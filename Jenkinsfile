pipeline {
    agent any
    environment {
	SSH_KEY = 'C:/ProgramData/Jenkins/.ssh/jenkins'
	SSH_CONFIG = 'C:/ProgramData/Jenkins/.ssh/config'
    }
	
    tools {
	maven 'Maven'
    }
	
    stages {
        stage('Checkout') {
            steps {
		echo 'Checkout...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
		echo 'Build...'
                bat "${tool 'Maven'}/bin/mvn clean package"
            }
        }

        stage('Deploy') {
            steps {
		echo 'Deploy...'
                bat '''
                scp -i "${env.SSH_KEY}" -F "${env.SSH_CONFIG}" target/library.war vuser@192.168.1.100:/tmp/
                ssh -i "${env.SSH_KEY}" -F "${env.SSH_CONFIG}" vuser@192.168.1.100 /usr/local/bin/deploy.sh /tmp/library.war
                '''
            }
        }
    }
	
	post {
        success {
            echo 'Successfully deployed'
        }
        failure {
            echo 'Deploy failed'
        }
    }
}
