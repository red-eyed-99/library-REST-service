pipeline {
    agent any
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
                sh 'mvn clean package'
            }
        }

        stage('Deploy') {
            steps {
				echo 'Deploy...'
                sh '''
                scp target/library.war vuser@192.168.1.100:/tmp/
                ssh vuser@192.168.1.100 /usr/local/bin/deploy.sh /tmp/library.war
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
