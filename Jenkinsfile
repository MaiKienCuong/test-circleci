void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "${GIT_URL}"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [
        [$class: "BetterThanOrEqualBuildResult", message: message, state: state, result: "1"],
        [$class: "BetterThanOrEqualBuildResult", message: "123", state: "FAILURE", result: "2"],
      ]]
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
    GITHUB_API_URL='https://api.github.com/repos/MaiKienCuong/test-circleci'
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
    }
  }

  post {
    success {
      echo "SUCCESSFUL"
      setBuildStatus("Build succeeded", "SUCCESS");
    }
    failure {
      echo "FAILED"
      setBuildStatus("Build failed", "FAILURE");
    }
  }
}