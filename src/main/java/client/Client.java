package client;

import com.sun.mail.pop3.POP3Folder;

import com.sun.mail.pop3.POP3Store;


import util.Utility;



import javax.mail.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import javax.swing.JOptionPane;

import java.io.*;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
	private static Client instance;


	private final Properties properties = new Properties();
	/**
	 * JavaMail-Session
	 */
	private Session pop3session;
	
	private Session Smtpsession;
	
	/**
	 * Folder of the specific chosen inbox from the mail server
	 */
	private POP3Folder emailInbox;
	/**
	 * Array of the fetched messages from the inbox.
	 */
	private Message[] messages;



//    private Transport transport = new SMTPSSLTransport(session, null);

	private MimeMessage mimeMessage;

	private Authenticator auth;



	private List<Message> newEmailList = new ArrayList<>();



	public static void setInstance(Client instance) {
		Client.instance = instance;
	}



	public List<Message> getNewEmailList() {
		return newEmailList;
	}


	public File getFiles() {
		return files;
	}

	public void setFiles(File files) {
		this.files = files;
	}


	private Client() {

	}

	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
			return instance;
		}
		return instance;
	}

	public void initConnectProperties(String host, String port/* , boolean secure */) {
	
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", port);
		properties.put("mail.pop3.connectiontimeout", "100");

		/*
		 * if (secure) { properties.put("mail.store.protocol", "pop3");
		 * properties.put("mail.pop3.ssl.enable", "true");
		 * properties.put("mail.pop3.starttls.enable", "true");
		 * properties.put("mail.pop3.ssl.socketFactory.class", "SSLSocket");
		 * 
		 * } else {
		 */
		properties.put("mail.store.protocol", "pop3");
		properties.put("mail.pop3.ssl.enable", "false");
		properties.put("mail.pop3.starttls.enable", "false");


	}

	public void initConnectPropertiesSmtp(String host, String port) {

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.connectiontimeout", "100");

		properties.put("mail.smtp.ssl.enable", "false");
		properties.put("mail.smtp.starttls.enable", "false");
		properties.put("mail.smtp.auth", "true");

	}

	public void connect(String username, String password) throws AuthenticationFailedException, MessagingException {

		pop3session = Session.getDefaultInstance(properties);
		POP3Store store = (POP3Store) pop3session.getStore((String) properties.get("mail.store.protocol"));

		store.connect(username, password);

		emailInbox = (POP3Folder) store.getFolder("INBOX");

		emailInbox.open(Folder.READ_ONLY);


	}



	public void connectSmtp(String username, String password) throws AuthenticationFailedException, MessagingException {

		auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};

		Smtpsession = Session.getInstance(properties, auth);

	}



	/*
	 * public boolean nameCheckGui(String name) { if (!Utility.nameCheck(name)) {
	 * infoBox("enter a valid name", ""); return false; } else return true;
	 * 
	 * }
	 */

	public boolean emailcheckGui(String email, String error) {
		if (email.equalsIgnoreCase("post@domain.Id") || email.isEmpty() || !Utility.emailCheck(email)) {
			Utility.infoBox(error, "");
			return false;
		} else
			return true;
	}

	public boolean portcheckGui(String port1, String port2) {
		if (!Utility.isPositiveInteger(port1) || !Utility.isPositiveInteger(port2)) {

			Utility.infoBox("Port wasn't a valid number!" + " Try Again", "nummeric error");
			return false;
		} else {
			return true;
		}

	}

	public void setMessageHeaders() throws MessagingException {

		mimeMessage.addHeader("Content-type", "text/plain; charset=UTF-8");
		mimeMessage.addHeader("format", "flowed");
		mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");

	}

	public void sendmailWithCC(String from, String subject, String body, String name, Address cc, boolean ccbolean,
			Address... recipients) throws UnsupportedEncodingException, MessagingException {
		mimeMessage = new MimeMessage(Smtpsession);
		mimeMessage.setFrom(new InternetAddress(from, name));
		//mimeMessage.setReplyTo(InternetAddress.parse(from, false));
		mimeMessage.addRecipients(Message.RecipientType.TO, recipients);
		mimeMessage.setSubject(subject);
		mimeMessage.setText(body);
		if (!ccbolean) {
			mimeMessage.addRecipient(RecipientType.CC, cc);
		}
		setMessageHeaders();
		mimeMessage.setSentDate(new Date());
		Transport.send(mimeMessage);

	}

	public List<Message> getMessages() throws MessagingException {
		updateMessages();
		return Arrays.asList(messages);
	}

	public POP3Folder getEmailInbox() {
		return emailInbox;
	}


	public String readMessage(Part messageSegment) throws MessagingException, IOException {
		StringBuilder sb = new StringBuilder();
		readMessageUtil(messageSegment, sb);

		new SecureRandom().nextInt();

		return sb.toString();
	}

	/**
	 * Utility-Method to fetch the message
	 *
	 * @param messageSegment Message object to read.
	 * @param sb             String-builder to use.
	 * @throws MessagingException If Connection-error occurred.
	 * @throws IOException        If Connection-error occurred.
	 */
	private void readMessageUtil(Part messageSegment, StringBuilder sb) throws MessagingException, IOException {
		if (messageSegment instanceof Message)
			sb.append(getMessageEnvelope((Message) messageSegment));

		// plain text message
		if (messageSegment.isMimeType("text/plain")) {
			sb.append("\n").append((String) messageSegment.getContent()).append("\n");
		}
		// Check multipart message
		else if (messageSegment.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) messageSegment.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				readMessageUtil(multipart.getBodyPart(i), sb);
			}
		}
		// check if the content is a nested message
		else if (messageSegment.isMimeType("message/rfc822")) {
			sb.append("Nested-Message:").append("\n");
			readMessageUtil((Part) messageSegment.getContent(), sb);
		}
		// read image attachment
		else if (messageSegment.isMimeType("image/jpeg")) {
			InputStream is = (InputStream) messageSegment.getContent();
			byte[] buffer = is.readAllBytes();
			FileOutputStream fos = new FileOutputStream("output.jpg");
			fos.write(buffer);
			fos.close();
		}
	}

	/**
	 * Get the envelope of the message.
	 *
	 * @param message Message to inspect
	 * @return A String containing the envelope data.
	 * @throws MessagingException If Connection-error occurred.
	 */
	private String getMessageEnvelope(Message message) throws MessagingException {
		StringBuilder builder = new StringBuilder();

		Address[] addresses;

		if ((addresses = message.getFrom()) != null)
			for (Address address : addresses)
				builder.append("From:").append(" ").append(address.toString()).append("\n");

		if ((addresses = message.getRecipients(Message.RecipientType.TO)) != null)
			for (Address address : addresses)
				builder.append("To:").append(" ").append(address.toString()).append("\n");

		if (message.getSubject() != null)
			builder.append("Subject: ").append(message.getSubject()).append("\n");

		if (message.getDescription() != null)
			builder.append("Description: ").append(message.getDescription()).append("\n");

		return builder.toString();
	}

	public void updateMessages() throws MessagingException {
	
		
			messages = emailInbox.getMessages();
			FetchProfile fetchProfile = new FetchProfile();
			fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
			fetchProfile.add("X-mailer");
			emailInbox.fetch(messages, fetchProfile);

			
		
	}

	
	public void reconnect(String username, String password, String host, String port) throws MessagingException {
		initConnectProperties(host, port);
		connect(username, password);

	}


	private File files ;




	private void readMessageUtilSaved(Part messageSegment, StringBuilder sb) throws MessagingException, IOException {
		if (messageSegment instanceof Message) sb.append(getMessageEnvelopeSaved((Message) messageSegment));

		// plain text message
		if (messageSegment.isMimeType("text/plain")) {
			sb.append("\n").append((String) messageSegment.getContent()).append("\n");
		}
		// Check multipart message
		else if (messageSegment.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) messageSegment.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				readMessageUtilSaved(multipart.getBodyPart(i), sb);
			}
		}
		// check if the content is a nested message
       /* else if (messageSegment.isMimeType("message/rfc822")) {
            sb.append("Nested-Message:").append("\n");
            readMessageUtil((Part) messageSegment.getContent(), sb);
        }*/
		// read image attachment

	}

	/**
	 * Get the envelope of the message.
	 *
	 * @param message Message to inspect
	 * @return A String containing the envelope data.
	 * @throws MessagingException If Connection-error occurred.
	 */
	private String getMessageEnvelopeSaved(Message message) throws MessagingException {
		StringBuilder builder = new StringBuilder();

		Address[] addresses;

		if ((addresses = message.getFrom()) != null) for (Address address : addresses)
			builder.append("NewMessage......").append("\n").append("From:....").append(" ").append(address.toString()).append("\n");

		if ((addresses = message.getRecipients(Message.RecipientType.TO)) != null) for (Address address : addresses)
			builder.append("To:....").append(" ").append(address.toString()).append("\n");

		if (message.getSubject() != null) builder.append("Subject:....").append(message.getSubject()).append("\n");
		else builder.append("Subject:....").append("this Message has no subject").append("\n");


		return builder.toString();
	}


	public String readMessageSaved(Part messageSegment) throws MessagingException, IOException {
		StringBuilder sb = new StringBuilder();
		readMessageUtilSaved(messageSegment, sb);


		return sb.toString();
	}


	public void backUp(String username) throws IOException, MessagingException {
		creatFolder(username);
		updateMessages();

		BufferedWriter writer = new BufferedWriter(new FileWriter(files));

		int i = 0;

		for (Message mes : emailInbox.getMessages()) {
			writer.write(readMessageSaved(mes));
			System.out.println(i++);

		}
		writer.write("NewMessage......");
		writer.newLine();
		writer.write("Finished:.................");
		writer.flush();
		writer.close();



	}

	public void backUpAfter(String username) throws IOException, MessagingException {
		creatFolder(username);
		updateMessages();
		newEmailList = new ArrayList<>();
		BufferedWriter writer = new BufferedWriter(new FileWriter(files , true));
		List<Message> temp = readMessageFromBinFile();

		Utility.removeWordFromFile(files.getAbsolutePath());
		Utility.removeWordFromFile(files.getAbsolutePath());
		int n = emailInbox.getMessages().length - temp.size()  ;
		for (int i = 0; i < n; i++) {
			writer.write(readMessageSaved(emailInbox.getMessages()[(temp.size() + i)]));
			newEmailList.add(emailInbox.getMessages()[(temp.size() + i)]);
		}


		writer.write("NewMessage......");
		writer.newLine();
		writer.write("Finished:.................");
		writer.flush();

		writer.close();
		System.out.println("finished");

	}





	public List<Message> readMessageFromBinFile() throws IOException, MessagingException {
		Scanner reader = new Scanner(files);

		String line;
		boolean isContent = false;
		String subject = "";
		StringBuilder content = new StringBuilder();
		String from = "";
		String to = "";
		List<Message> mes = new ArrayList<>();

		MimeMessage mim = new MimeMessage((Session) null);


		int i = 0;


		while (!((line = reader.nextLine()).equals("Finished:................."))) {


			if (line.startsWith("From:....")) {

				from = line.substring(9);
				mim.setFrom(from);


			} else if (line.startsWith("To:....")) {
				to = line.substring(7);
				mim.addRecipient(Message.RecipientType.TO, new InternetAddress(to, false));


			} else if (line.startsWith("Subject:....")) {
				subject = line.substring(12);
				mim.setSubject(subject);


			} else if (line.startsWith("NewMessage......")) {

				isContent = false;
				mim.setText(content.toString());
				content = new StringBuilder();
				mes.add(mim);


				mim = new MimeMessage((Session) null);


			} else if (!line.isEmpty() && !line.startsWith("Subject:....") && !line.startsWith("From:....") && !line.startsWith("To:....") && !line.startsWith("NewMessage......")) {

				isContent = true;
				content.append(line).append("\n");


			} else if (isContent) {

				content.append(line).append("\n");

			}


		}


		reader.close();
		mes.remove(0);
		return mes;

	}

	public boolean checkIfFileExist() {
		return files.exists();
	}

	public void creatFolder(String username) throws IOException {
		String path = System.getProperty("user.dir") + "\\" + username;
		files = new File(path + "\\"+  "NewEmail.txt") ;
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
			if(files.exists()){
				files.createNewFile();
			}

		}

	}



	

}
