import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main2 {

	public static void main(String[] arges) throws Exception {


		read();

//		decodeUrl("%E6%9C%88%E8%8F%AFprintlnE7%9F%B3%E8%B3%BC%E5%85%A5");

	}


	private static void read() throws Exception {

		File file = new File("/tmp/applog.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String row = br.readLine();
		while (row != null) {
			row = br.readLine();
			if (row == null)
				continue;

			String url = row.split(" ")[6];

			if (StringUtils.indexOf(url, "?") < 0)
				continue;

			String queryString = url.split("\\?")[1];

			String[] ketValues = queryString.split("&");

			for (int i = 0; i < ketValues.length; i++) {

				if (StringUtils.indexOf(url, "=") < 0)
					continue;
				String[] keyValue = ketValues[i].split("=", 2);

				if (keyValue.length < 2)
					continue;

				String key = keyValue[0];
				String value = keyValue[1];

				if (key.equals("_carrier") || key.equals("_sim_carrier") || key.equals("_referrer") || key.equals("_model") || key.equals("label") || key
						.equals("Name"))
					continue;

				decodeUrl(value);



			}
		}

		br.close();

	}

	private static List<Charset> decodeCharsets;

	static {

		decodeCharsets = new ArrayList<Charset>();

		decodeCharsets.add(Charset.forName("ISO-2022-JP"));

		decodeCharsets.add(Charset.forName("EUC-JP"));

		decodeCharsets.add(Charset.forName("UTF-8"));

		decodeCharsets.add(Charset.forName("windows-31j"));

		decodeCharsets.add(Charset.forName("Shift_JIS"));

	}

	public static String decodeUrl(String url) throws UnsupportedEncodingException {

		if (url == null || url.trim().equals(""))
			return url;


		if(url.matches("^[a-zA-Z]+$")) return null;
		//


		// ISO-8859-1でデコードしバイトを取得

		byte[] b = null;

		try {

			b = URLDecoder.decode(url, "iso-8859-1").getBytes("iso-8859-1");

		} catch (IllegalArgumentException e) {

			// %の形式不正などの場合

			// ※ エラー時の処理

		}

		// それぞれの文字コードで文字列構築を試行する

		for (Charset charset : decodeCharsets) {

			CharsetDecoder decoder = charset.newDecoder();

			try {

				if(ByteBuffer.wrap(b) == null) continue;
				String ret = decoder.decode(ByteBuffer.wrap(b)).toString();
				String enc = decoder.charset().toString();
				if(enc.equals("ISO-2022-JP")) continue;
				if(enc.equals("EUC-JP")) continue;
				if(enc.equals("UTF-8")) continue;
				if(enc.equals("windows-31j")) continue;
				System.out.println(enc + ": " + ret);
				return ret;

			} catch (CharacterCodingException e) {
				continue;

			}

		}

//		return new String(b);
		return "other";
	}

}

