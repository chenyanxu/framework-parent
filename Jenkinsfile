// refer to https://github.com/siamaksade/cart-service/blob/jenkinsfiles/Jenkinsfile
node('maven') {
  stage('Build framework-parent App') {
    git url: "https://github.com/chenyanxu/framework-parent.git"
    sh "mvn install -DskipTests=true -s settings.xml"
  }
  stage('Deploy framework-parent App') {
    sh "mvn deploy -DskipTests=true -s settings.xml"
  }
}
