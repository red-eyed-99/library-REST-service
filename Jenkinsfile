pipeline {
    agent any
	
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
                scp -v -i %SSH_KEY% target/library.war vuser@192.168.1.100:/tmp/
                ssh -F C:\\ProgramData\\Jenkins\\.ssh\\config vm vuser@192.168.1.100 /usr/local/bin/deploy.sh /tmp/library.war
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
