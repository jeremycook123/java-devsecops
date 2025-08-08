pipeline {
    agent {
        node {
            label 'java'
        }
    }

    tools {
        maven 'maven'
        jdk 'jdk21'
    }

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "nexus:8081"
        NEXUS_REPOSITORY = "cloudacademy-releases"
        NEXUS_CREDENTIAL_ID = "nexus"
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

        stage ('Sonar') {
            steps {
                withCredentials([string(credentialsId: 'sonar', variable: 'sonartoken')]) {
                    sh '''
                        mvn clean compile -DskipTests=true sonar:sonar \
                          -Dsonar.projectKey=customer-bankapp \
                          -Dsonar.host.url=http://${SONARQUBE_HOST} \
                          -Dsonar.login=${sonartoken}
                    '''
                }
                // timeout(time: 2, unit: 'MINUTES') {
                //     script {
                //         def qg = waitForQualityGate()
                //         if (qg.status != 'OK') {
                //             error "Pipeline aborted due to failed SonarQube Quality Gate: ${qg.status}"
                //         }
                //     }
                // }
            }
        }

        stage('Dependency-Track') {
            steps {
                withCredentials([string(credentialsId: 'dependency-track', variable: 'API_KEY')]) {
                    dependencyTrackPublisher artifact: 'target/bom.xml', projectName: 'customer-bankingapp', projectVersion: '1.0.2', synchronous: true, dependencyTrackApiKey: API_KEY, autoCreateProjects: true
                }
            }
        }

        stage('Nexus Publish') {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
            }
        }

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
    }
}