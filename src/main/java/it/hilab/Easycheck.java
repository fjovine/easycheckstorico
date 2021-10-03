package it.hilab;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Classe che esegue il comando per interfacciarsi con Easycheck.
 * La sintassi è descritta dal metodo help() cui si rimanda.
 */
public class Easycheck {
    /***
     * Sinossi del comando
     */
    public static void help() {
        System.out.println("java -jar easycheck.jar help");
        System.out.println("java -jar easycheck.jar ip {comando}");
        System.out.println("Programma che si collaga allo strumento EasyCheck scaricando in formato JSON alcune informazioni da condividere");
        System.out.println("con il sistema informativo aziendale.");
        System.out.println("  ip è l'indirizzo IP dello strumento, visibile in basso a destra dello");
        System.out.println("     schermo Easycheck in formato a.b.c.d dove a,b,c e d sono numeri da 1 a 255");
        System.out.println("  {comando} è uno dei seguenti possibili comandi: listaEsami, ultimoTest, scarica, attendiTest ");
        System.out.println("");
        System.out.println("Sintassi dei comandi");
        System.out.println("  listaEsami [{anno} [{mese} [{giorno}]]] emette un array json contenente i codici degli esami fatti nel periodo considerato");
        System.out.println("              se definito solo anno, emette i codici tutti gli esami effettuati nell'anno considerato");
        System.out.println("              se definito anno e mese, emette i codici tutti gli esami effettuati nel mese considerato");
        System.out.println("              se definito anno, mese e giorno emette i codici tutti gli esami effettuati nel giorno considerato");
        System.out.println("");
        System.out.println("  ultimoTest  emette una singola variabile json contenente l'ultimo test effettuato");
        System.out.println("");
        System.out.println("  scarica {codice esame} emette una variabile strutturata JSON contenente i risultati dell'esame considerato");
        System.out.println("");
        System.out.println("  attendiTest richiede regolarmente il codice dell'ultmo test finché non termina un nuovo test");
        System.out.println("              che viene emesso in formato JSON sulla console");
        System.out.println("");
        System.out.println("  AVVERTENZE");
        System.out.println("              1) per codice esame si intende una stringa nel formato ");
        System.out.println("                 YYYYMMDDNNN in cui Y è l'anno, M il mese D il gionro e N il numero sequenziale del test.");
        System.out.println("                 Questi numeri sono riportati alla lunghezza di ciascun campo con zeri iniziali non significativi.");
        System.out.println("              2) i risultati del comando sono emessi sulla console (stdout) tranne se è presente il folder denominato");
        System.out.println("                 storico nel folder da dove si lancia il programma");
        System.exit(-1);
    }

    /**
     * Emette su stdout un messaggio di errore e visualizza la sinossi del comando.
     * @param error stringa di errore.
     */
    public static void error(String error) {
        System.out.println("Errore : "+error);
        System.out.println("");
        help();
    }

    /**
     * Controlla la sintassi della stringa passata che è un indirizzo UO
     * @param ip stringa che descrive un indirizzo ip.
     * @return true se la stringa è corretta sia sintatticamente (quattro campi numerici separatdi da punto) sia semanticamente (i numeri da 1 a 255);
     */
    private static boolean checkIpSyntax(String ip) {
        String ipPattern = "^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$";
        Pattern r = Pattern.compile(ipPattern);

        // Now create matcher object.
        Matcher m = r.matcher(ip);
        if (!m.find()) {
            return false;
        }

        for (int i=1; i<5; i++) {
            int n = Integer.parseInt(m.group(i));
            if (n>255) {
                return false;
            }
        }

        return true;
    }

    private static final int LISTA_ESAMI = 0;
    private static final int ULTIMO_TEST = 1;
    private static final int SCARICA = 2;
    private static final int ATTENDI = 3;

    /**
     * Mappa il nome del comando al corrispondente codice.
     */
    private static Map<String, Integer> commands = new HashMap<String,Integer>() {{
       put("listaEsami", LISTA_ESAMI);
       put("ultimoTest", ULTIMO_TEST);
       put("scarica", SCARICA);
       put("attendiTest", ATTENDI);
    }};

    /**
     * IP passato sulla linea di comando.
     */
    static String ip;

    /**
     * Se true, il folder storico esiste e vi viene salvato l'output invece di emetterlo su stdout.
     */
    private static boolean saveStorico = false;

    /**
     * Ritorna il numero se la stringa passata è un intero valido e se questo è nel range considerato.
     * @param n stringa numerica da controllare.
     * @param from valore minimo accettabile.
     * @param to valore massimo accettabile.
     * @return true se la stringa è numerica e il valore è nel range passato.
     */
    private static boolean checkInteger(String n, int from, int to) {
        try {
            int i = Integer.parseInt(n);
            if (i < from) {
                return false;
            }
            if (i > to) {
                return false;
            }
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Usando il protocollo HTTP, contatta easycheck, effettua la richiesta con la url passata e ritorna il JSON ricevuto sotto forma di stringa.
     * @param url contenente il comando da processaare.
     * @param savePath filename nel folder storico (se definito) dove salvare il risultato della query.
     * @return la stringa JSON ricevuta dallo strumento.
     */
    private static String remoteQuery(String url, String savePath) {
        String result = "";
        //System.out.println("Query : ["+url+"]");
        try {
            result = HttpGet.wGet(url);
        } catch (Exception e) {
            System.out.println("Eccezione "+e);
            e.printStackTrace();
        }

        if (saveStorico) {
            try {
                Files.write(Paths.get("./storico", savePath), result.getBytes(StandardCharsets.UTF_8));
            } catch (IOException ioe) {
                error("Impossibile salvare il contenuto del file");
            }
        } else {
            System.out.println(result);
        }

        return result;
    }

    /**
     * Ritorna la url di base per eseguire qualsiasi comando.
     * @return la url di base.
     */
    private static String getBasicUrl() {
        return String.format("http://%s:2080/", ip);
    }

    /**
     * Ritorna la url necessaria per eseguire il comando passato.
     * @param command Comando da eseguire.
     * @return la url da usare per effettuare una query.
     */
    private static String getBasicUrl(String command) {
        return String.format("http://%s:2080/%s", ip, command);
    }

    /**
     * Esegue il comando listaEsami.
     * @param arg argomenti passati sulla linea di comando.
     */
    public static void doListaEsami(String[] arg) {
        String url = getBasicUrl() + "JSONGETCALENDAR";
        if (arg.length >= 3) {
            String year = arg[2];
            if (!checkInteger(year, 2020, 2050)) {
                error("anno non valido.");
            }
            url += String.format("?year=%s", year);
        }

        if (arg.length >= 4) {
            String month = arg[3];
            if (!checkInteger(month, 1, 12)) {
                error("mese non valido.");
            }
            url += String.format("&month=%s", month);
        }

        if (arg.length == 5) {
            String day = arg[4];
            if (!checkInteger(day, 1, 31)) {
                error("giorno non valido.");
            }
            url += String.format("&day=%s", day);
        }

        if (arg.length > 5) {
            error("Formato del comando listaEsami non valido");
        }

        remoteQuery(url, "calendar.json");
    }

    /**
     * Richiede il codice dell'ultimo test effettuato.
     * @return Il codice numerico dell'ultimo test effettuato nel formato YYYYMMDDNNN dove Y è l'anno
     * M il mese D il giorno e N il numero sequenziale del test.
     */
    public static String ultimoEsame() {
        String ultimoEsame = remoteQuery(getBasicUrl("JSONLASTTEST"), "ultimo.json");
        String[] lines = ultimoEsame.split("\\r?\\n");
        String lastTest = lines[1].split(":")[1].trim();

        return lastTest.substring(1, lastTest.length()-1);
    }

    /**
     * Esegue il comando ultimoEsame
     * @param arg argomenti passati sulla linea di comando.
     */
    public static void doUltimoEsame(String[] arg) {
        if (arg.length > 3) {
            error("Non sono previsti ulteriori parametri nel comando ultimoEsame");
        }

        String ultimoEsame = ultimoEsame();
        remoteQuery(getBasicUrl(String.format("JSONGETTEST?test=%s", ultimoEsame)), ultimoEsame + ".json");
    }

    /**
     * Esegue il comando scarica
     * @param arg argomenti passati sulla linea di comando.
     */
    public static void doScarica(String[] arg) {
        if (arg.length > 3) {
            error("Il comando ultimoEsame prevede un solo parametro");
        }

        String code = arg[3];
        remoteQuery(getBasicUrl(String.format("JSONGETTEST?test=%s", code)), code+".json");
    }

    /**
     * Esegue il comando attendiTest.
     * Determina il codice dell'ultimo test effettuato e ogni minuto richiede il nuovo ultimo codice.
     * Quando riceve un valore diverso, allora è terminato un nuovo test e lo scarica in formato JSON.
     * @param arg argomenti passati sulla linea di comando.
     */
    public static void doAttendiTest(String[] arg)
    {
        String primo = ultimoEsame();
        System.out.println("Ultimo test : "+primo);
        System.out.println("In attesa del successivo");
        while (true) {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException ie) {
                break;
            }
            if (ultimoEsame().compareTo(primo) != 0) {
                break;
            }
        }
    }

    /**
     * Punto iniziale.
     * @param arg argomenti passati sulla linea di comando.
     */
    public static void main(String[] arg) {
        if (arg.length >= 1) {
            ip = arg[0];
            if (ip.compareToIgnoreCase("help") == 0) {
                help();
            }
        }

        if (arg.length < 2) {
            error("Errore di sintassi");
        }

        if (! checkIpSyntax(ip)) {
            error(String.format("[%s] non è un indirizzo IP valido.", ip));
        }

        saveStorico = Files.exists(Paths.get("./storico"));

        String command = arg[1];
        if (commands.containsKey(command)) {
            switch (commands.get(command)) {
                case LISTA_ESAMI:
                    doListaEsami(arg);
                    break;
                case ULTIMO_TEST:
                    doUltimoEsame(arg);
                    break;
                case SCARICA:
                    doScarica(arg);
                    break;
                case ATTENDI:
                    doAttendiTest(arg);
                    break;
            }
        } else {
            error(String.format("Il comando [%s] non è supportato.", command));
        }
    }
}