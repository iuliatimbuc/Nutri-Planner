# Nutri Planner

Nutri Planner este o aplicatie web full-stack de planificare nutritionala care ajuta utilizatorii sa isi monitorizeze dieta, greutatea si consumul de apa.

## Functionalitati

- **Autentificare** - in cont personal cu mail si parola
- **Jurnal alimentar** - logarea meselor pe zile (mic dejun, pranz, cina, snack) cu vizualizarea macronutrientilor
- **Tracker apa** - monitorizarea consumului zilnic de apa
- **Inregistrare si calculul automan al goalurilor** - cont personalizat cu date fizice (varsta, inaltime, greutate, goal) care
calculeaza calorii zilnice, proteine, carbohidrati si grasimi pe baza datelor utilizatorului folosind formula Mifflin-St Jeor
- **Tracker greutate** - inregistrarea greutatii zilnice cu grafic de evolutie si calcul BMI
- **Calendar** - vizualizarea istoricului nutritional pe luna cu indicatori colorati (verde = in limita, rosu = depasit)
- **Conversatie in timp real** - sistem de mesagerie intre utilizatori cu WebSocket, istoric conversatii si cautare utilizatori dupa nume
- **Notificari** - notificari automate in browser la orele meselor (mic dejun, gustare, pranz, gustare, cina) folosind WebSocket si Spring Scheduler

## Cum rulezi aplicatia

**Backend autentificare (port 8082):**
Deschide proiectul in IntelliJ si ruleaza clasa `AuthApplication.java`

**Backend principal (port 8080):**
Deschide proiectul in IntelliJ si ruleaza clasa `NutriPlannerApplication.java`

**Frontend:**
```
cd frontend
npm install
npm start
```

Aplicatia ruleaza pe `http://localhost:3000`  
Backend principal: `http://localhost:8080`  
Backend autentificare: `http://localhost:8082`
