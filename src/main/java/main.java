import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;

import javax.swing.plaf.synth.SynthToolTipUI;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;

public class main {

	public static void main(String[] arges) throws Exception {

		int cnt = 1;
		File file = new File("/tmp/app20180904");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String row = br.readLine();
		while (row != null) {
			row = br.readLine();
			if (row == null)
				continue;

			String url = row.split(" ")[6];

			if (StringUtils.indexOf(url, "?") < 0)
				continue;

			String[] tmp = url.split("\\?");

			if (tmp == null || tmp.length < 2)
				continue;

			String queryString = tmp[1];

			String[] ketValues = queryString.split("&");

			for (int i = 0; i < ketValues.length; i++) {

				if (StringUtils.indexOf(url, "=") < 0)
					continue;
				String[] keyValue = ketValues[i].split("=", 2);

				if (keyValue.length < 2)
					continue;

				String key = keyValue[0];
				String value = keyValue[1];

				if (isShiftJis(key, value)) {
					System.out.println(cnt++);
					System.out.println(key + "=" + value);
				}

			}
		}

		br.close();

	}

	public static boolean isShiftJis(String key, String src) {

		URLCodec utf8 = new URLCodec("UTF-8");
		URLCodec shiftJis = new URLCodec("Shift_JIS");

		try {

			String decodedValueUtf8 = utf8.decode(src);
			String encodedValueUtf8 = utf8.encode(decodedValueUtf8);

			if (StringUtils.equals(src.toLowerCase(), encodedValueUtf8.toLowerCase())) {
				return false;
			}

			String decodedValueShiftJis = shiftJis.decode(src);
			String encodedValueShiftJis = shiftJis.encode(decodedValueShiftJis);

			if (StringUtils.equals(decodedValueUtf8.toLowerCase(), decodedValueShiftJis.toLowerCase())){
				return false;
			}

			if (StringUtils.equals(src.toLowerCase(), encodedValueShiftJis.toLowerCase())) {
				System.out.println("isShiftJis=true --------------------");
				System.out.println("src = [" + key + "=" + src + "]");
				System.out.println("utf8 decode = [" + decodedValueUtf8 + "]");
				System.out.println("shiftJis decode = [" + decodedValueShiftJis + "]");
				return true;
			} else {
				System.out.println("isShiftJis=false --------------------");
				System.out.println("src = [" + key + "=" + src + "]");
				System.out.println("utf8 decode = [" + decodedValueUtf8 + "]");
				System.out.println("shiftJis decode = [" + decodedValueShiftJis + "]");

			}

			return false;

		} catch (DecoderException | EncoderException e) {
			return false;
		}

	}

}
