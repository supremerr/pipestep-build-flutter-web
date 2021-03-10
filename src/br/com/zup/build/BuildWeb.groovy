
package br.com.zup.build

class BuildWeb{
    def call (jenkins) {
        jenkins.podTemplate(
            containers: [
                jenkins.containerTemplate(
                    name: 'flutter', 
                    image: 'cirrusci/flutter:2.0.1', 
                    ttyEnabled: true, 
                    command: 'cat'
                )
            ],
            yamlMergeStrategy: jenkins.merge(),
            workspaceVolume: jenkins.persistentVolumeClaimWorkspaceVolume(
                claimName: "pvc-${jenkins.env.JENKINS_AGENT_NAME}",
                readOnly: false
            )
        )

        {
            jenkins.node(jenkins.POD_LABEL){
                jenkins.container('flutter'){
                    try {
                        jenkins.sh label: "Build Web", 
                                   script: "flutter build web"
                    } catch (Exception e) {
                        jenkins.unstable("AN error occured during build step. Please, verify the logs.")
                    }
                    
                }
            }
        }
    }
}