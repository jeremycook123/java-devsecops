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
                sh 'mvn clean compile' 
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

        stage('DependencyTrack') {
            steps {
                withCredentials([string(credentialsId: 'dependency-track', variable: 'API_KEY')]) {
                    sh '''
                        mvn -e cyclonedx:makeBom dependency-track:upload-bom \
                            -Ddependency-track.apiKey=${API_KEY}
                    '''
                }
            }
        }
    }
}