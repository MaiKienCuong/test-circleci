void setBuildStatus(String context, String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "${GIT_URL}"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
  ]);
}

void setBuildStatusPending(String context, String message){
  step([
      $class: "GitHubSetCommitStatusBuilder",
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
      statusMessage: [content: message]
  ]);
}

pipeline {

  agent any

  environment {
    DOCKER_IMAGE = "cuongmaikien/test-java-jenkins"
  }

  stages {
    stage("Test") {
      steps {
        setBuildStatusPending("Test", "Test stage is running");
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
        setBuildStatusPending("Build", "Build stage is running");
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
      setBuildStatus("ci/jenkins", "Ci success", "SUCCESS")
    }
    failure {
      echo "FAILED"
      setBuildStatus("ci/jenkins", "Ci failure", "FAILURE")
    }
  }
}