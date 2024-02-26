pipeline {
    agent any

    tools { 
        maven 'maven'
        jdk 'jdk17'
    }

    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                ''' 
            }
        }
        
        stage('Checkout') {
            steps {
                git url: 'https://github.com/jeremycook123/java-devsecops.git', branch: 'main'
            }
        }        

        stage ('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }

            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
            
        // stage ('Sonar') {
        //     steps {
        //         withCredentials([string(credentialsId: 'sonar', variable: 'sonartoken')]) {
        //             sh '''
        //                 mvn clean compile -DskipTests=true sonar:sonar \
        //                   -Dsonar.projectKey=java-devsecops \
        //                   -Dsonar.host.url=http://${SONARQUBE_HOST} \
        //                   -Dsonar.login=${sonartoken}
        //             '''
        //         }
        //     }
        // }

        stage('BOM') {
            steps {            
                sh '''
                    mvn -e org.cyclonedx:cyclonedx-maven-plugin:makeBom
                '''
            }

            post {
                always {
                    archiveArtifacts artifacts: 'target/bom.xml', fingerprint: true
                }
            }
        }

        stage('Dependency-Track') {
            steps {
                withCredentials([string(credentialsId: 'dependency-track', variable: 'API_KEY')]) {
                    dependencyTrackPublisher artifact: 'target/bom.xml', projectName: 'customer-bankingapp', projectVersion: '1.0.2', synchronous: true, dependencyTrackApiKey: API_KEY, autoCreateProjects: true
                }
            }
        }
    }
}