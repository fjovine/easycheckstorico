# EasyCheckStorico

Utilità per scaricare i dati in formato JSON dal Easycheck.

Perché questo programma funzioni è necessario che il software del EasyCheck sia almeno 
la versione 1.261. Aggiornarla se è precedente.

## Generalità
Easycheck è collegato in rete attraverso vari meccanismi tra cui un server HTTP che risponde alla porta 2080.
Le informazioni relative agli esami sono scaricabili attraverso la seguente API.

|Comando|Descrizione|
|:----|:---|
JSONGETTEST?test={testcode} | Ritorna un JSON che contiene i parametri del codice richiesto. Il {testcode} è una stringa di formato YYYYMMDDNNN dove Y è l'anno, M il mese D il giorno e N il numero sequenziale del test nella giornata |
|JSONGETCALENDAR| Ritorna un array JSON con i codici di tutti i test effettuati nella vita dello strumennto|
|JSONGETCALENDAR?year={anno}|Ritorna un array JSON con i codici dei test effettuati nell'anno passato a parametro|
|JSONGETCALENDAR?year={anno}&month={mese}|Ritorna un array JSON con i codici dei test effettuati nel mese dell'anno passato a parametro|
|JSONGETCALENDAR?year={anno}&month={mese}&day={giorno}|Ritorna un array JSON con i codici dei test effettuati nel giorno passato a parametro|
|JSONLASTTEST|Ritorna il codice dell'ultimo test effettuato|

Questo è un software java a riga di comando che si interfaccia allo strumento e scarica i dati usando correttamente
l'API considerata.

Si tratta di un software di riferimento che può essere integrato così come è o usato per creare altri software 
(per esempio in python o javascript) a seconda delle necessità dell'utente finale.

## Sintassi della command line

`java -jar easycheck.jar help`

`java -jar easycheck.jar ip {comando}`

* **ip** è l'indirizzo IP dello strumento, visibile in basso a destra dello schermo Easycheck in formato a.b.c.d dove a,b,c e d sono numeri da 1 a 255
* **{comando}** è uno dei seguenti possibili comandi: 
  * **listaEsami**, 
  * **ultimoTest**
  * **scarica**, 
  * **attendiTest**

### Sintassi dei singoli comandi

`listaEsami [{anno} [{mese} [{giorno}]]]` 

emette un array json contenente i codici degli esami fatti nel periodo considerato,

* se definito solo anno, emette i codici tutti gli esami effettuati nell'anno considerato,
* se definito anno e mese, emette i codici tutti gli esami effettuati nel mese considerato,
* se definito anno, mese e giorno emette i codici tutti gli esami effettuati nel giorno considerato,

`ultimoTest`

mette una singola variabile json contenente l'ultimo test effettuato,

`scarica {codice esame}`

emette una variabile strutturata JSON contenente i risultati dell'esame considerato,

`attendiTest` richiede regolarmente il codice dell'ultmo test finché non termina un nuovo test,
che viene emesso in formato JSON sulla console

**AVVERTENZE**

1. Per **codice esame** si intende una stringa nel formato **YYYYMMDDNNN** in cui 
   * **Y** è l'anno
   * **M** il mese 
   * **D** il giorno 
   * **N** il numero sequenziale del test nel giorno
    
Questi numeri sono riportati alla lunghezza di ciascun campo con zeri iniziali non significativi.

2. I risultati del comando sono emessi sulla console (stdout) a meno che non sia presente il folder denominato **storico**
nel folder da dove si lancia il programma. In tal caso non viene emesso nulla sullo stdout ma un file
viene generato all'interno del folder **storico** 
   
**COMPILAZIONE**
Il software è pronto per essere messo in fork dall'interno di IntelliJIdea community edition (https://www.jetbrains.com/idea/download)
Tuttavia è costituito di due classi nel folder storico quindi è molto facile compilare direttamente usando
il compilatore **javac** su linea di comando. 
