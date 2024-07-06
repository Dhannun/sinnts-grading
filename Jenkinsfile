pipeline {
    agent any

    tools {
        maven 'maven3'
        jdk 'jdk17'
    }

    environment {
        DB_URL =   credentials('db-url')
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')
        JWT_EXPIRY = '172800000'
        JWT_SECRET_KEY = credentials('jwt-secret')
        PORT = '2024'
        REFRESH_TOKEN_EXPIRY = '2592000000'

        SCANNER_HOME = tool 'sonar-scanner'
    }

    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/Dhannun/sinnts-grading.git'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar') { // the (credentialsId: 'sonar-token') was replaced becuse we will use the sonar server itself, which has the server ip address and the sonar-token instean of just the token. that is the sonar you configured in Manage Jenkins > System
                    sh '''
                     $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectKey=grading -Dsonar.projectName=grading \
                     -Dsonar.java.binaries=.
                    '''
                }
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: ' --scan ./', odcInstallation: 'DC' // Path to check (pom.xlm)
                dependencyCheckPublisher pattern: './dependency-check-report.xml' // Report pattern
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Deploy Artifact to Nexus') {
            steps {
                withMaven(globalMavenSettingsConfig: 'global-maven', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
                    sh 'mvn deploy '
                }
            }
        }


    }
}