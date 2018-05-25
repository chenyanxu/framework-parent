// refer to https://github.com/siamaksade/cart-service/blob/jenkinsfiles/Jenkinsfile
node('maven') {
  stage('Build Framework App') {
    git url: "https://github.com/chenyanxu/framework-parent.git"
    sh "mvn install -DskipTests=true -s settings.xml"
  }
  stage('Deploy Framework App') {
    sh "mvn deploy -DskipTests=true -s settings.xml"
  }
}
