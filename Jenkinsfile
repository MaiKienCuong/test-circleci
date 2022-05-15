void setBuildStatus(String context, String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "${GIT_URL}"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
  ]);
}

void setBuildStatusPending(){
  step([
      $class: "GitHubSetCommitStatusBuilder",
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      statusMessage: [content: "Building"]
  ]);
}

pipeline {

  agent any

  environment {
    DOCKER_IMAGE = "cuongmaikien/test-java-jenkins"
  }

  stages {
    stage("Set pending"){
      steps{
        setBuildStatusPending();
      }
    }

    stage("Test") {
      steps {
        sh "pwd"
        sh "ls"
        sh '''chmod +x mvnw && sed -i 's/\r$//' mvnw && ./mvnw test'''
      }
      post{
        success{
          setBuildStatus("Test", "Test success", "SUCCESS")
        }
        failure{
          setBuildStatus("Test", "Test failure", "FAILURE")
        }
      }
    }

    stage("build") {
      environment {
        DOCKER_TAG="${GIT_BRANCH.tokenize('/').pop()}-${GIT_COMMIT.substring(0,7)}"
      }
      steps {
        sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} . "
        sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
        sh "docker image ls | grep ${DOCKER_IMAGE}"
        withCredentials([usernamePassword(credentialsId: 'docker-hub-credential', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
            sh 'echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin'
            sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
            sh "docker push ${DOCKER_IMAGE}:latest"
        }

        //clean to save disk
        sh "docker image rm ${DOCKER_IMAGE}:${DOCKER_TAG}"
        sh "docker image rm ${DOCKER_IMAGE}:latest"
      }
      post{
        success{
          setBuildStatus("Build", "Build success", "SUCCESS")
        }
        failure{
          setBuildStatus("Build", "Build failure", "FAILURE")
        }
      }
    }
  }

  post {
    success {
      echo "SUCCESSFUL"
    }
    failure {
      echo "FAILED"
    }
  }
}