package storeapplication

import grails.transaction.Transactional;

class UserController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

	def scaffold = User 
	
	def create() {
		respond new User(params)
	}
	
	@Transactional
	def save(User userInstance) {
		if (userInstance == null) {
			notFound()
			return
		}

		if (userInstance.hasErrors()) {
			respond userInstance.errors, view:'create'
			return
		}

		userInstance.save flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.created.message',
					args: [message(code: 'user.label',
						default: 'User'), userInstance.id])
				redirect userInstance
			}
			'*' { respond userInstance, [status: CREATED] }
		}
	}
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}		
	}
	
}
