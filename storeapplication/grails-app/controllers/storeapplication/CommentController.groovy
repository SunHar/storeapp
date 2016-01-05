package storeapplication

import grails.transaction.Transactional;

class CommentController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

	def scaffold = Comment 
	
	def create() {
		respond new Comment(params)
	}
	
	@Transactional
	def save(Comment commentInstance) {
		if (commentInstance == null) {
			notFound()
			return
		}

		if (commentInstance.hasErrors()) {
			respond commentInstance.errors, view:'create'
			return
		}

		commentInstance.save flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.created.message',
					args: [message(code: 'user.label',
						default: 'User'), commentInstance.id])
				redirect commentInstance
			}
			'*' { respond commentInstance, [status: CREATED] }
		}
	}
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'comment.label', default: 'Comment'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}		
	}
	
}
