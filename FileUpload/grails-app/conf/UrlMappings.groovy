class UrlMappings {

	static mappings = {
        "/rest/person"( 
           controller: 'person',
           action: 'createPerson',
           method: 'POST'
        )
        "/rest/person"(
           controller: 'person',
           action: 'patientList',
           method: 'GET'
        )
        "/rest/person/image"(
           controller: 'person',
           action: 'download',
           method: 'GET'
        )
            
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
