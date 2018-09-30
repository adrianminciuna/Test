package test;

import java.util.ArrayList;
import java.util.List;

public class Message {
		private String fieldName;
		private String message;
		private List<Message> details;
		
		public Message(String fieldName, String message) {
			super();
			this.fieldName = fieldName;
			this.message = message;
		}
		
		public Message() {
			// TODO Auto-generated constructor stub
		}

		public Message addMessage(Message inner) {
			if(details == null) {
				details = new ArrayList<>();
			}
			details.add(inner);
			return this;
		}
		
		public Message addMessage(String fieldName, String message) {
			return addMessage(new Message(fieldName, message));
		}
		
		public Message findOrCreate(String name) {
			return getOrCreateDetais().stream().filter(p->p.fieldName != null && p.fieldName.equals(name)).findFirst()
				.orElse( addMessage(new Message(name, null)));
		}
		
		public Message findOrCreateList(String name, int index) {
			Message parentList = findOrCreate(fieldName);
			List<Message> list = parentList.getOrCreateDetais();
			while (list.size() <= index )
				list.add(new Message());
			return list.get(index);
		}
		public Message findOrCreateMap(String name, String key) {
			Message parentList = findOrCreate(fieldName);
			List<Message> list = parentList.getOrCreateDetais();
			return list.stream().filter(p->p.fieldName != null && p.fieldName.equals(name)).findFirst()
					.orElseGet(() -> { 
								Message a = new Message(key, null); 
								list.add(a); 
								return a;
							});
		}
		
		private List<Message> getOrCreateDetais(){
			if(details == null) {
				details = new ArrayList<>();
			}
			return details;
		}
		
		
		
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public List<Message> getDetails() {
			return details;
		}
		public void setDetails(List<Message> details) {
			this.details = details;
		}

		public Message findOrCreate(String name, Integer listIndex, Object mapKey) {
			if(listIndex != null ) {
				return findOrCreateList(name, listIndex);
			} else if(mapKey != null ) {
				return findOrCreateMap(name, mapKey.toString());
			} else {
				return findOrCreate(name);
			}
		}
		
		
}
