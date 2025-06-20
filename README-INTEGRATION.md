# Sensor Integration - Documentation Complète

## 🚀 Vue d'ensemble

Le projet **Sensor Integration** est une application Spring Boot complète pour la surveillance, le diagnostic et la maintenance prédictive de machines industrielles équipées de capteurs. L'application intègre **Temporal.io** pour l'orchestration de workflows et propose une architecture moderne et scalable.

## 🏗️ Architecture

### Stack Technologique
- **Backend**: Spring Boot 3.5.0 + Java 17
- **Base de données**: PostgreSQL (production) + H2 (tests)
- **Orchestration**: Temporal.io 1.24.0
- **Sécurité**: Spring Security + JWT OAuth2
- **Documentation API**: SpringDoc OpenAPI (Swagger)
- **Génération PDF**: iTextPDF 7.2.5
- **Validation**: Jakarta Validation + Hibernate Validator

### Architecture en couches
```
sensor-integration/
├── api/                    # APIs externes
├── business/               # Logique métier
│   ├── activity/          # Activities Temporal
│   ├── dto/               # Data Transfer Objects
│   ├── mapper/            # Mappers
│   ├── service/           # Services métier
│   └── workflow/          # Workflows Temporal
├── core/                  # Configuration centrale
│   ├── config/           # Configurations
│   ├── enums/            # Énumérations
│   ├── exception/        # Gestion d'exceptions
│   └── model/            # Modèles centraux
├── data/                  # Couche d'accès aux données
│   ├── entity/           # Entités JPA
│   └── repository/       # Repositories Spring Data
├── presentation/          # Couche présentation
│   └── Controller/       # Contrôleurs REST
├── security/             # Sécurité et authentification
├── service/              # Services applicatifs
└── utils/                # Utilitaires
```

## 🗃️ Modèle de données

### Entités principales
- **Machine**: Machines industrielles (UUID)
- **Capteur**: Capteurs attachés aux machines (température, pression, vibration)
- **DonneeCapteur**: Données horodatées des capteurs
- **DiagnosticHistory**: Historique des diagnostics avec workflows
- **Alerte**: Système d'alertes automatiques
- **Rapport**: Rapports de diagnostic générés
- **Utilisateur**: Gestion des utilisateurs avec rôles

### Rôles utilisateur
- `TECHNICIAN`: Techniciens de maintenance
- `SUPERVISOR`: Superviseurs d'équipe
- `ADMINISTRATOR`: Administrateurs système

## 🔄 Temporal.io - Workflows

### Workflows implémentés

#### 1. DiagnosticWorkflow
- **handleDiagnostic**: Traitement automatisé des diagnostics
- **updateDiagnosticStatus**: Mise à jour du statut
- **scheduleMaintenance**: Programmation de maintenance

#### 2. Activities
- **DiagnosticActivity**: Opérations de diagnostic
- **MaintenanceActivity**: Opérations de maintenance

### Configuration Temporal
```properties
temporal.server.address=localhost:7233
temporal.namespace=sensor-integration
temporal.task-queue=sensor-queue
```

## 🚦 APIs REST

### Machines (`/api/machines`)
- `GET /` - Liste toutes les machines
- `GET /{id}` - Récupère une machine par ID
- `POST /` - Crée une nouvelle machine
- `PUT /{id}` - Met à jour une machine
- `DELETE /{id}` - Supprime une machine
- `GET /search/by-location?localisation=X` - Recherche par localisation
- `GET /search/by-name?nom=X` - Recherche par nom

### Capteurs (`/api/capteurs`)
- `GET /` - Liste tous les capteurs
- `GET /{id}` - Récupère un capteur par ID
- `POST /` - Crée un nouveau capteur
- `PUT /{id}` - Met à jour un capteur
- `DELETE /{id}` - Supprime un capteur
- `GET /machine/{machineId}` - Capteurs d'une machine
- `GET /search/by-type?type=X` - Recherche par type

### Données des capteurs (`/api/donnees-capteurs`)
- `GET /` - Liste toutes les données
- `GET /{id}` - Récupère une donnée par ID
- `POST /` - Ajoute une nouvelle donnée
- `GET /capteur/{capteurId}` - Données d'un capteur
- `GET /capteur/{capteurId}/latest?limit=10` - Dernières données
- `GET /capteur/{capteurId}/average` - Moyenne des valeurs
- `GET /capteur/{capteurId}/out-of-threshold` - Données hors seuils

### Diagnostics (`/api/diagnostics`)
- `POST /` - Crée un diagnostic
- `GET /machine/{machineId}` - Diagnostics d'une machine
- `GET /type/{diagnosticType}` - Diagnostics par type
- `GET /date-range?startDate=X&endDate=Y` - Diagnostics par période

### Workflows Temporal (`/api/workflows`)
- `POST /diagnostic/start` - Démarre un workflow de diagnostic
- `POST /diagnostic/execute-sync` - Exécute un diagnostic synchrone
- `PUT /diagnostic/{workflowId}/status` - Met à jour le statut
- `POST /maintenance/schedule` - Programme une maintenance
- `POST /diagnostic/bulk-start` - Traitement en lot
- `GET /{workflowId}/status` - Statut d'un workflow
- `POST /{workflowId}/cancel` - Annule un workflow
- `POST /{workflowId}/terminate` - Termine un workflow

### Rapports (`/api/rapports`)
- `POST /generer/{machineId}` - Génère un rapport
- `GET /exporter/{rapportId}` - Exporte en PDF
- `GET /ia/{machineId}` - Rapports par machine et générateur
- `GET /rechercher?motCle=X` - Recherche par mot-clé
- `GET /alertes-non-resolues/{machineId}` - Alertes non résolues

## 🔐 Sécurité

### Configuration JWT
```properties
app.jwt-secret=your-secret-key
```

### Endpoints publics
- `/swagger-ui/**` - Documentation Swagger
- `/api-docs/**` - Documentation OpenAPI
- `/accounts/signIn` - Connexion

## 🗄️ Configuration de base de données

### PostgreSQL (Production)
```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/Diagnostic
spring.datasource.username=postgres
spring.datasource.password=31janvier2005
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### H2 (Tests)
Configuration automatique pour les tests avec profil approprié.

## 🐳 Docker Compose

### Services configurés
```yaml
services:
  temporal-server:
    image: temporalio/auto-setup:1.24.0
    ports:
      - "7233:7233"  # Temporal Server API
      - "8233:8233"  # Temporal Web UI
    environment:
      - NAMESPACE=sensor-integration
      - TASK_QUEUE=sensor-queue
```

## 🚀 Démarrage rapide

### 1. Prérequis
- Java 17+
- PostgreSQL 12+
- Docker (pour Temporal)

### 2. Configuration de la base de données
```sql
CREATE DATABASE Diagnostic;
```

### 3. Démarrage de Temporal
```bash
docker-compose up -d
```

### 4. Compilation et démarrage
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

### 5. Accès aux interfaces
- **Application**: http://localhost:8000/api/
- **Swagger UI**: http://localhost:8000/api/swagger-ui.html
- **Temporal Web UI**: http://localhost:8233

## 📊 Génération de données de test

L'application génère automatiquement des données de test au démarrage via `TemporalInitializationService`:
- 6 machines de test (Moulage et Soufflage)
- Diagnostics classiques pour chaque machine
- Workflows Temporal automatiques
- Maintenances programmées

## 🔧 Fonctionnalités avancées

### 1. Surveillance des seuils
- Vérification automatique des valeurs des capteurs
- Génération d'alertes en cas de dépassement
- Logging des anomalies

### 2. Maintenance prédictive
- Analyse des données historiques
- Programmation automatique de maintenances
- Workflows Temporal pour l'orchestration

### 3. Rapports intelligents
- Génération automatique de rapports
- Export PDF avec iTextPDF
- Analyse comparative des données

### 4. Workflows distribués
- Orchestration avec Temporal.io
- Gestion des erreurs et retry automatique
- Workflows longue durée pour la maintenance

## 🧪 Tests et qualité

### Compilation
```bash
./mvnw clean compile -DskipTests
```

### Tests (à implémenter)
```bash
./mvnw test
```

## 📚 Documentation API

L'API est entièrement documentée avec OpenAPI/Swagger :
- **URL**: http://localhost:8000/api/swagger-ui.html
- **JSON**: http://localhost:8000/api/api-docs

## 🛠️ Développement

### Ajout d'un nouveau capteur
1. Créer le type dans les énumérations
2. Configurer les seuils appropriés
3. Implémenter la logique d'analyse spécifique

### Ajout d'un nouveau workflow
1. Définir l'interface dans `business/workflow/`
2. Implémenter la logique métier
3. Créer les activities nécessaires
4. Enregistrer dans `TemporalConfiguration`

### Ajout d'une nouvelle API
1. Créer le contrôleur dans `presentation/Controller/`
2. Implémenter le service dans `service/`
3. Ajouter la validation et documentation

## 🔍 Monitoring et observabilité

### Logs applicatifs
- Configuration logback pour différents niveaux
- Logs structurés pour les workflows Temporal
- Monitoring des erreurs métier

### Métriques Temporal
- Interface web Temporal sur le port 8233
- Suivi des workflows en temps réel
- Historique des exécutions

## 🚨 Gestion des erreurs

### Patterns implémentés
- Retry automatique dans les workflows
- Circuit breaker pour les services externes
- Gestion centralisée des exceptions
- Logging structuré des erreurs

## 📋 TODO / Améliorations futures

- [ ] Tests unitaires et d'intégration complets
- [ ] Monitoring avec Micrometer/Prometheus
- [ ] Cache Redis pour les performances
- [ ] Authentification OAuth2 externe
- [ ] Interface utilisateur React/Angular
- [ ] Notifications en temps réel (WebSocket)
- [ ] Machine Learning pour la prédiction
- [ ] API GraphQL alternative

## 🤝 Contribution

Pour contribuer au projet :
1. Fork le repository
2. Créer une branche feature
3. Implémenter les modifications
4. Ajouter les tests appropriés
5. Soumettre une Pull Request

## 📞 Support

Pour toute question ou problème :
- Vérifier la documentation Swagger
- Consulter les logs Temporal
- Vérifier la configuration de base de données
- S'assurer que Temporal est démarré

---

**Auteur**: Équipe Sensor Integration  
**Version**: 1.0.0  
**Date**: 2024  
**License**: Propriétaire