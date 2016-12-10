package mx.prisma.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class StrutsDateConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map arg0, String[] arg1, Class arg2) {
		String dia = arg1[0].substring(0, 2);
		String mes = arg1[0].substring(3,5);
		String anio = arg1[0].substring(6);
				
		Date fecha = null;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			fecha = formatter.parse(dia + "/" + mes + "/" + anio);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return fecha;
	}

	@Override
	public String convertToString(Map arg0, Object arg1) {
		return arg1.toString();
	}

}
