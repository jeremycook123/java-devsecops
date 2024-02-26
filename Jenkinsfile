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
                    dependencyTrackPublisher artifact: 'target/bom.xml', projectName: 'my-project', projectVersion: 'my-version', synchronous: true, dependencyTrackApiKey: API_KEY, projectProperties: [tags: ['tag1', 'tag2'], swidTagId: 'my swid tag', group: 'my group', parentId: 'parent-uuid']
                }
            }
        }
    }
}