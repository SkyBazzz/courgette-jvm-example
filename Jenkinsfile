pipeline {
    agent {
        label 'linux'
    }

    stages {
        stage ('git') {
        steps {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], gitTool: 'ubuntu', submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'ad0b666c-4d40-49c0-9b95-c9e62e0fc695', url: 'https://github.com/SkyBazzz/courgette-jvm-example.git']]])
            }
        }
        stage ('Compile Stage') {
            steps {
                    sh 'mvn clean compile'
                }
        }
        stage ('Testing Stage') {
            steps {
                    sh 'mvn test'
            }
        }
    }
}