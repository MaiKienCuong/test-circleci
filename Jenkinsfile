void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "${GIT_URL}"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [
        [$class: "AnyBuildResult", message: message, state: state],
        [$class: "AnyBuildResult", message: message, state: state],
      ]]
  ]);
}

pipeline {

  agent any

  environment {
    DOCKER_IMAGE = "cuongmaikien/test-java-jenkins"
    GITHUB_API_URL='https://api.github.com/repos/MaiKienCuong/test-circleci'
  }

  stages {
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
//       withCredentials([usernamePassword(credentialsId: '9f4a5f82-8155-4acf-b977-fcf832850582', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
//         sh 'curl -X POST --user $USERNAME:$PASSWORD --data  "{\\"state\\": \\"success\\"}" --url $GITHUB_API_URL/statuses/$GIT_COMMIT'
//       }
      setBuildStatus("Build succeeded", "SUCCESS");
    }
    failure {
      echo "FAILED"
//       withCredentials([usernamePassword(credentialsId: '9f4a5f82-8155-4acf-b977-fcf832850582', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
//         sh 'curl -X POST --user $USERNAME:$PASSWORD --data  "{\\"state\\": \\"failure\\"}" --url $GITHUB_API_URL/statuses/$GIT_COMMIT'
//       }
      setBuildStatus("Build failed", "FAILURE");
    }
  }
}