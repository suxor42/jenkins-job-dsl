def createJobs(String applicationname, Map config){
    def environments = ['staging', 'prod']
    environments.each {
        String environment -> createJob(environment, applicationname, config)
    }
}

def createJob(String environment, String applicationname, Map config){
    println("$applicationname-$environment: $config")
}

@Grab(group='org.yaml', module='snakeyaml', version='1.5')
import org.yaml.snakeyaml.Yaml
def yaml = new Yaml()
def object = yaml.load(new File('test_data.yml').text)
assert object instanceof Map



object.applications.each {
    String applicationame, Map config -> createJobs(applicationame, config)
}


