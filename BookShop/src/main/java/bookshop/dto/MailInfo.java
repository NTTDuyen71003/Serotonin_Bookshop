package bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailInfo {
	String from;
	String to;
	String subject;
	String body;
	String attachments;

	public MailInfo(String to, String subject, String body) {
		this.from = "Serotonin Book Shop <Serotoninsupport@gmail.com>";
		this.to = to;
		this.subject = subject;
		this.body = body;
	}
}
