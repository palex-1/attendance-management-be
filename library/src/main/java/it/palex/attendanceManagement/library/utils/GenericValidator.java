package it.palex.attendanceManagement.library.utils;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * @author Alessandro Pagliaro
 *
 */
public class GenericValidator {
	
	public static final int MAX_CODICE_FISCALE_LENGTH = 16;
	public static final String CODICE_FISCALE_REGEX="^(?:(?:[B-DF-HJ-NP-TV-Z]|[AEIOU])[AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[1256LMRS][\\dLMNP-V])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$";
//	private static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	public static final String TELEFONO_REGEX = "(\\+\\d{1,3}( ))?(\\s)?[0-9/-]{3,16}";
	public static final int MAX_TELEFONO_LENGHT = 20;
	
	public static final String CAP_REGEX = "[0-9]{5}";
	public static final String NUMER_REGEX = "[0-9]+";
	public static final String SDI_REGEX = "[a-zA-Z0-9]{7}";
	
	
	public static boolean isANumber(String num) {
		if(num==null) {
			return false;
		}
		return num.matches(NUMER_REGEX);
	}
	
	
	/**
	 * @param telefono
	 * @return
	 */
	public static boolean isValidNumeroTelefono(String telefono) {
		if(telefono==null){
			return false;
		}
		if(telefono.length()>MAX_TELEFONO_LENGHT || !telefono.matches(TELEFONO_REGEX)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param email
	 * @return true if the mail is a valid email false otherwise<br>
	 * Note if email is null false will be returned
	 */
	public static boolean isValidEmail(String email){
		if(email==null){
			return false;
		}
		return EmailValidator.getInstance().isValid(email);
	}
	
	/**
	 * 
	 * @param cf
	 * @return true if the CF is <strong>potentially</strong> valid, false if the CF is not valid(assured)<br>
	 * Note: This is a naif test cause is only a regex mathes.
	 */
	public static boolean isAValidCodiceFiscale_NAIF_TEST(String cf){
    	if(cf==null){
    		return false;
    	}
    	if(cf.length()>MAX_CODICE_FISCALE_LENGTH) {
    		return false;
    	}
    	String park=cf.toUpperCase();
    	return park.matches(CODICE_FISCALE_REGEX);
    }
		
	/**
	 * 
	 * @param number
	 * @return true if is a valid piva number false otherwise
	 */
	public static boolean isValidItalianPIVANumber(String number){
		if(number==null) {
			return false;
		}
        String vat = number.replaceAll("[ -]+", "").toUpperCase().trim();
		
		if(!vat.matches("[0-9]{11}")){
			return false;
		}
		
		return isValidCarattereControlloFormulaLuhn(number);
	}
	
	/*calcola il carattere di controllo usando la formula di Luhn
	   Sia X la somma delle prime cinque cifre in posizione dispari
       Sia Y la somma dei doppi delle cinque cifre in posizione pari, sottraendo 9 se il doppio della cifra è superiore a 9
		Sia T=(X+Y) mod 10 l'unità corrispondente alla somma dei numeri sopra calcolati
		Allora la cifra di controllo C = (10-T) mod 10
	 */
	private static boolean isValidCarattereControlloFormulaLuhn(String iva){
		String number= iva.trim();
		
		if(number==null){
			throw new NullPointerException();
		}
		if(!number.matches("[0-9]{11}")){
			return false;
		}
			
		int X = sommaCifrePostoDispari(number);
		int Y = calcolaSommaDeiDoppiInPosizionePariSottraendo9SeIlDoppioDellaCifraSuperioreA9(number);
		
		int T = (X + Y) % 10;
		
		int C = (10 - T) % 10;
		int x11 = Integer.parseInt(number.charAt(10)+"");

		return x11 == C;
	}
	
	private static int calcolaSommaDeiDoppiInPosizionePariSottraendo9SeIlDoppioDellaCifraSuperioreA9(String number) {
		  int x2 = Integer.parseInt(number.charAt(1)+"");
		  int x4 = Integer.parseInt(number.charAt(3)+"");
		  int x6 = Integer.parseInt(number.charAt(5)+"");
		  int x8 = Integer.parseInt(number.charAt(7)+"");
		  int x10 = Integer.parseInt(number.charAt(9)+"");
		  
		  int somma1 = calcolaSommaDeiDoppiSottraendo9SeIlDoppioDellaCifraSuperioreA9(x2);
		  int somma2 = calcolaSommaDeiDoppiSottraendo9SeIlDoppioDellaCifraSuperioreA9(x4);
		  int somma3 = calcolaSommaDeiDoppiSottraendo9SeIlDoppioDellaCifraSuperioreA9(x6);
		  int somma4 = calcolaSommaDeiDoppiSottraendo9SeIlDoppioDellaCifraSuperioreA9(x8);
		  int somma5 = calcolaSommaDeiDoppiSottraendo9SeIlDoppioDellaCifraSuperioreA9(x10);
	      return somma1 + somma2 + somma3 + somma4 + somma5;	  
	}
		
	private static int calcolaSommaDeiDoppiSottraendo9SeIlDoppioDellaCifraSuperioreA9(int x){
		if(x*2>9){
			  return x*2 -9; 
		}
	   	return x*2;
	}

	private static int sommaCifrePostoDispari(String number){
		int x1 = Integer.parseInt(number.charAt(0)+"");
		int x3 = Integer.parseInt(number.charAt(2)+"");
		int x5 = Integer.parseInt(number.charAt(4)+"");
		int x7 = Integer.parseInt(number.charAt(6)+"");
		int x9 = Integer.parseInt(number.charAt(8)+"");
		return (x1+x3+x5+x7+x9);
	}

	/**
	 * @param cap
	 * @return true if cap is valid<br>
	 * note that is a naif test cause the validation is made by a regex, it be a numeric string of five char
	 */
	public static boolean isValidCAP(String cap) {
		if(cap==null){
			return false;
		}
		if(!cap.matches(CAP_REGEX)){
			return false;
		}
		
		return true;
	}
	public static boolean isValidFatturazioneElettronicaSdi(String sdi) {
		if(sdi==null) {
			return false;
		}
		if(!sdi.matches(SDI_REGEX)){
			return false;
		}
		
		return true;
	}

}

