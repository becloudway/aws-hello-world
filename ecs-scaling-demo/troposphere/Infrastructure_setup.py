from troposphere import GetAtt, Output, Parameter, Ref, Template, FindInMap, cloudformation, Base64, Join, autoscaling
from troposphere.cloudwatch import Alarm, MetricDimension
from troposphere.sns import Subscription, Topic
from troposphere.sqs import Queue
from troposphere.ecs import Cluster, Environment, TaskDefinition, LoadBalancer, Service, ContainerDefinition, Volume, PortMapping
from troposphere.iam import Policy, Role, PolicyType, InstanceProfile
from troposphere.autoscaling import AutoScalingGroup, LaunchConfiguration
from troposphere.awslambda import Function, Code, EventSourceMapping
import awacs
import awacs.aws
import awacs.sts

import sys

t = Template()

t.add_description(
	"Infrastructure for ECS scaling demo.")
	
myqueue = t.add_resource(Queue("MyQueue"))
	
instancerole = t.add_resource(
	Role(
		"InstanceRole",
		AssumeRolePolicyDocument=awacs.aws.Policy(
			Statement=[
				awacs.aws.Statement(
					Effect=awacs.aws.Allow,
					Action=[awacs.sts.AssumeRole],
					Principal=awacs.aws.Principal("Service", ["ec2.amazonaws.com"])
				)
			]
		),
		Path="/",
		Policies=[
			Policy(
				PolicyName="ecs-service",
				PolicyDocument=awacs.aws.Policy(
					Statement=[
						awacs.aws.Statement(
							Effect=awacs.aws.Allow,
							Action=[awacs.aws.Action("ecs", "*")],
							Resource=["*"],
						),
						awacs.aws.Statement(
							Effect=awacs.aws.Allow,
							Action=[awacs.aws.Action("ecr", "*")],
							Resource=["*"],
						)
					]
				)
			)
		]
	)
)

instanceprofile = t.add_resource(
	InstanceProfile(
		"InstanceProfile",
		Roles=[Ref("InstanceRole")],
		Path="/"
	)
)

t.add_mapping('RegionMap', {
    "us-east-1": {"AMI": "ami-2b3b6041"},
    "eu-west-1": {"AMI": "ami-03238b70"}
})

subnetid = t.add_parameter(Parameter("SubnetId", Type="List<AWS::EC2::Subnet::Id>"))

keyname = t.add_parameter(Parameter("KeyName", Type= "AWS::EC2::KeyPair::KeyName"))

launchconfiguration = t.add_resource(
	LaunchConfiguration(
		"LaunchConfiguration",
		ImageId=FindInMap("RegionMap", Ref("AWS::Region"), "AMI"),
		InstanceType="t2.micro",
		KeyName=Ref("KeyName"),
		IamInstanceProfile=Ref("InstanceProfile"),
		Metadata=autoscaling.Metadata(
					cloudformation.Init({
						'config': cloudformation.InitConfig(
							commands={
								'add_instance': {
									'command': Join("", ["#!/bin/bash\n sudo /bin/su -c 'echo ECS_CLUSTER=", Ref("mycluster"), " >> /etc/ecs/ecs.config'"])
								}
							},			
							files=cloudformation.InitFiles({
								"/etc/cfn/cfn-hup.conf": cloudformation.InitFile(
									content=Join('',["[main]\nstack=", Ref("AWS::StackId"), "\nregion=", Ref("AWS::Region"), "\n"]),
									mode="000400",
									owner="root",
									group="root"
								),
								"/etc/cfn/hooks.d/cfn-auto-reloader.conf" : cloudformation.InitFile(
									content=Join("",["[cfn-auto-reloader-hook]\ntriggers=post.update\npath=Resources.ContainerInstances.Metadata.AWS::CloudFormation::Init\naction=/opt/aws/bin/cfn-init -v --stack ", Ref("AWS::StackName"), "--resource ContainerInstances --region ", Ref("AWS::Region"), "\nrunas=root\n"])
								)
							}),
							services={
								"sysvinit": cloudformation.InitServices({
									'cfn-hup': cloudformation.InitService(
										enabled=True, 
										ensureRunning=True, 
										files=['/etc/cfn/cfn-hup.conf', '/etc/cfn/hooks.d/cfn-auto-reloader.conf']
									)
								})
							}
						)
					})
				),
		UserData=Base64(Join('', [
							"#!/bin/bash -xe\n",
							"yum install -y aws-cfn-bootstrap\n",
							" /opt/aws/bin/cfn-init -v ",
							"    --stack ", Ref("AWS::StackName"),
							"    --resource LaunchConfiguration"
							"    --region ", Ref("AWS::Region"), "\n",						
							"/opt/aws/bin/cfn-signal -e $? ",
							"    --stack ", Ref("AWS::StackName"),
							"    --resource AutoScalingGroup"
							"    --region ", Ref("AWS::Region"), "\n",
						])),
	)
)

autoscalinggroup = t.add_resource(
	AutoScalingGroup(
		"AutoScalingGroup",
		MaxSize= "1",
        MinSize= "1",
        VPCZoneIdentifier=Ref(subnetid),
        LaunchConfigurationName=Ref(launchconfiguration),
        DesiredCapacity="1"
	)
)

cluster = t.add_resource(Cluster("mycluster"))

containerdefinition = ContainerDefinition(
	"ContainerDefinition",
	Cpu="10",
	Essential="true",
	Image="335317431711.dkr.ecr.us-east-1.amazonaws.com/demo:latest",
	Memory="100",
	Name="demo-test-cont-def2",
	Environment=[
		Environment(Name="AWS_ACCESS_KEY_ID", Value=sys.argv[1]),
		Environment(Name="AWS_SECRET_ACCESS_KEY", Value=sys.argv[2]),
		Environment(Name="REGION", Value=sys.argv[3]),
		Environment(Name="SQS_URL", Value=Ref(myqueue))
	]
)


taskdefinition = t.add_resource(
	TaskDefinition(
		"TaskDefinition",
		ContainerDefinitions=[containerdefinition],
		Volumes=[]
	)
)

service = t.add_resource(
	Service(
	"ECSService",
	DesiredCount="1",
	Cluster=Ref("mycluster"),
	TaskDefinition=Ref("TaskDefinition"),
	DependsOn=["AutoScalingGroup"]
	)
)

scalinglambda = t.add_resource(Function(
    "ScalingLambda",
    Code=Code(
		S3Bucket="demo-xti",
		S3Key="ecs-scaler-0.0.1-SNAPSHOT.jar"
    ),
    Handler="com.xti.awspresentation.demo.scaler.Scaler::handler",
    Role=GetAtt("LambdaExecutionRole", "Arn"),
    Runtime="java8",
    Timeout=30,
    MemorySize=1536
))

lambdaexecutionrole = t.add_resource(Role(
    "LambdaExecutionRole",
    Path="/",
    Policies=[Policy(
        PolicyName="root",
        PolicyDocument={
            "Version": "2012-10-17",
            "Statement": [{
                "Action": ["logs:*"],
                "Resource": "arn:aws:logs:*:*:*",
                "Effect": "Allow"
            },
            {
                "Action": ["ecs:*"],
                "Resource": "*",
                "Effect": "Allow"
            }
            ]
        })],
    AssumeRolePolicyDocument={"Version": "2012-10-17", "Statement": [
        {"Action": ["sts:AssumeRole"], "Effect": "Allow",
         "Principal": {"Service": ["lambda.amazonaws.com"]}}]},
))

alarmtopic = t.add_resource(
	Topic(
	"AlarmTopic"
	)
)

t.add_output([
	Output(
	"QueueURL",
	Description="URL of newly created SQS Queue",
	Value=Ref(myqueue)
	),
	Output(
	"QueueARN",
	Description="ARN of newly created SQS Queue",
	Value=GetAtt(myqueue, "Arn")
	),
	Output(
	"QueueName",
	Description="Name newly created SQS Queue",
	Value=GetAtt(myqueue, "QueueName")
	),
	Output(
	"ecsservice",
	Value=Ref("ECSService")
	),
	Output(
	"ecscluster",
	Value=Ref("mycluster")
	),
	Output(
	"taskdef",
	Value=Ref("TaskDefinition")
	)
])

print(t.to_json())
