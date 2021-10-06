## Variabili e tipo

|Cal|Iso|Ts|Ca|Identificatore || Tipo | Descrizione |
|:--|:--|:-|:-|:--------------------|:-------|:------------------|
|C| | | | P_CCAL_A | semplice| Coefficiente A della calibrazione di conducibilità |
|C| | | | P_CCAL_B | semplice| Coefficiente B della calibrazione di conducibilità |
| |I|T|A| P_COMMENT | stringa | Commento aggiunto al test|
|C| | | | P_COND_CAL | array double |Calibrazione: conducibilità calibrata del punto  di calibrazione i-esimo. |
|C| | | | P_COND_RAW | array double |Calibrazione: conducibilità non calibrata in decimi di millivolt del punto  di calibrazione i-esimo. |
| |I| |A| P_C_DELTA | double | variazione di conducibilitâ della fase isoterma |
| |I| |A| P_C_FIN | double | conducibilità finale |
| |I| |A| P_C_INI | double | conducibilitä iniziale|
| | |T| | P_C_MAX | double| Conducibilità massima test TS senza THK|
| | |T| | P_C_MAX_T | double| Conducibilità massima test TS con THK|
| | |T| | P_C_MIN | double| Conducibilità minima test TS senza THK|
| | |T| | P_C_MIN_T | double | Conducibilità minima test TS con THK|
| | |T| | P_D_MAX |double | Delta conducibilità massima test TS |
| |I| | | P_ISO_D_COND | array double | variazione di conducibilità isoterma in uS del punto i-esimo|
| |I| | | P_ISO_TEMP | float | Temperatura a cui avviene la precipitazione isoterma in °C |
| |I| | | P_ISO_TIME | array double| Tempi della fase isoterma in secondi dall'inizio della precipitazione del punto i-esimo |
| |I|T|A| P_SAMPLE_CODE | string | codice del campione|
| |I|T|A| P_SER_NO | string |Numero di serie dello strumento |
| | |T| | P_S_TIME | intero | tempo di stabilizzazione isoterma in minuti per test TS |
|C| | | | P_TCAL_A | semplice| Coefficiente A della calibrazione termica |
|C| | | | P_TCAL_B | semplice| Coefficiente B della calibrazione termica |
| | | | | P_TEMP_CAL | array unidimensionale|Calibrazione: temperatura letta esternamente del punto di calibrazione i-esimo. |
| | | | | P_TEMP_RAW |strutturata |Calibrazione:temperature temperatura misurata internamente in decimi di millivolt del punto di calibrazione i-esimo. |
| | | | | P_TIME_SHOW | semplice | Intervallo in secondi tra due letture consecutive |
| | | | | P_TSS | | |
| | | | | P_TS_COND |strutturata |Conducibilità misurata durante la fase isoterma senza THK |
| | | | | P_TS_COND_T |strutturata |Conducibilità misurata durante la fase isoterma con THK |
| | | | | P_TS_TEMP |strutturata |Temperature misurate durante la fase isoterma TS |
| | | | | P_TS_TIME | | |
| | | | | P_T_INIZIO | semplice| Data e ora di partenza del test nel formato "dd/MM/yy HH:mm"|
| | | | | S_TEMP_MAX | semplice| Temperatura massima in °C per il test TS |
| | | | | S_TEMP_MIN | semplice| Temperatura minima in °C per il test TS |


| | |T| | P_IS_LAST_TS_VALID | boolean | True se la ts calcolata è valida |
| | | | | P_IS_TCC_VALID | boolean | True se i parametri relativi alla tcc sono validi |
| | | | | P_IS_TSS_VALID | boolean | ??? |
| | | | | P_LAST_TS | boolean | Utlimo valore  della TS|
| | |T| | P_SSS | | |
| | | | | P_TCC | | |
