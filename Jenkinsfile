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

        DOCKER_IMAGE = 'dhannun/apps'
        BUILD_TAG = "grading-v1.0.0-${env.BUILD_ID}"
    }

    stages {
        stage('Cleaning Workspace') {
            cleanWs()
        }

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

        stage('Quality Gate Check') {
            steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
                }
            }
        }

//         stage('OWASP Dependency Check') {
//             steps {
//                 // Ensure Dependency-Check runs and generates the report
//                 sh 'mvn org.owasp:dependency-check-maven:check -Dformat=XML -DoutputDirectory=.'
//                 // Collect OWASP Dependency-Check report
//                 dependencyCheckPublisher pattern: '**/dependency-check-report.xml' // Ensure the pattern matches the report location
//             }
//         }

        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '--scan ./', odcInstallation: 'DC' // Path to check (pom.xlm)
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml' // Report pattern
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

        stage('Build and Tag Docker Image') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${BUILD_TAG} ."
                }
            }
        }

//         stage ('Docker Image Scan') {
//             steps {
//                 sh "trivy image --format table -o trivy-image-scan.html ${DOCKER_IMAGE}:${BUILD_TAG}"
//             }
//         }

        stage('Push to Docker Registry [Docker Hub]') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker') {
                        sh "docker push ${DOCKER_IMAGE}:${BUILD_TAG}"
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                def jobName = env.JOB_NAME
                def buildNumber = env.BUILD_NUMBER
                def pipelineStatus = currentBuild.result ?: 'UNKNOWN'
                def bannerColor = pipelineStatus.toUpperCase() == 'SUCCESS' ? 'green' : 'red'

//                 def body = """ // Aditya
//                     <html>
//                         <body>
//                             <div style="border: 4px solid ${bannerColor}; padding: 10px;">
//                                 <h2>${jobName} - Build ${buildNumber}</h2>
//                                 <div style="background-color: ${bannerColor}; padding: 10px;">
//                                     <h3 style="color: white;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h3>
//                                 </div>
//                                 <p>Check the <a href="${BUILD_URL}">console output</a>.</p>
//                             </div>
//                         </body>
//                     </html>
//                 """

                def body = """
                    <html>
                        <body>
                            <div style="background-color: #FFA07A; padding: 10px; margin-bottom: 10px;">
                                <p style="color: white; font-weight: bold;">Project: ${env.JOB_NAME}</p>
                            </div>
                            <div style="background-color: #90EE90; padding: 10px; margin-bottom: 10px;">
                                <p style="color: white; font-weight: bold;">Build Number: ${env.BUILD_NUMBER}</p>
                            </div>
                            <div style="background-color: #87CEEB; padding: 10px; margin-bottom: 10px;">
                                <p style="color: white; font-weight: bold;">URL: ${env.BUILD_URL}</p>
                            </div>
                        </body>
                    </html>
                """

                emailext (
                    subject: "${jobName} - Build ${buildNumber} - ${pipelineStatus.toUpperCase()}",
                    body: body,
                    to: 'abudukhanyunus@gmail.com',
                    from: 'jenkins@example.com',
                    replyTo: 'jenkins@example.com',
                    mimeType: 'text/html',
//                     attachmentsPattern: 'trivy-image-report.html'
                )
            }
        }
    }
}