package demsy.ynby.security;

import com.kmetop.demsy.security.IPasswordEncoder;

public class MD5PasswordEncoder implements IPasswordEncoder {
	private MD5 md5 = new MD5();

	public String encodePassword(String rawPass, Object salt) {
		return md5.getMD5ofStr(rawPass);
	}

	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		String pass1 = "" + encPass;
		String pass2 = encodePassword(rawPass, salt);

		return pass1.toLowerCase().equals(pass2.toLowerCase());
	}

	@Override
	public boolean isValidPassword(String encPass, String rawPass, Object salt) {
		return isPasswordValid(encPass, rawPass, salt);
	}

}
