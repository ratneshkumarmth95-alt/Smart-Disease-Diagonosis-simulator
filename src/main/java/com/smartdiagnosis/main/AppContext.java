package com.smartdiagnosis.main;

import com.smartdiagnosis.database.DatabaseConnection;
import com.smartdiagnosis.database.DatabaseInitializer;
import com.smartdiagnosis.model.User;
import com.smartdiagnosis.repository.*;
import com.smartdiagnosis.service.*;
import com.smartdiagnosis.ui.LoginUI;
import com.smartdiagnosis.utils.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;

/**
 * AppContext - Application-wide state container (Singleton).
 * Holds the currently logged-in user and all service/DAO instances.
 * This avoids passing objects around everywhere.
 * Demonstrates: Singleton Pattern, Dependency Management
 */
public class AppContext {

    private static final Logger logger = Logger.getLogger(AppContext.class.getName());
    private static AppContext instance;

    // Currently logged-in user
    private User currentUser;

    // DAOs (database access objects)
    public final UserDAO       userDAO       = new UserDAO();
    public final HistoryDAO    historyDAO    = new HistoryDAO();
    public final HealthCheckDAO healthCheckDAO = new HealthCheckDAO();
    public final SymptomLogDAO symptomLogDAO = new SymptomLogDAO();
    public final JournalDAO    journalDAO    = new JournalDAO();

    // Services
    public final KnowledgeBase    knowledgeBase    = KnowledgeBase.getInstance();
    public final DiagnosisEngine  diagnosisEngine  = new DiagnosisEngine();
    public final AdviceEngine     adviceEngine     = new AdviceEngine();
    public final GamificationService gamificationService = new GamificationService();
    public final QuizService      quizService      = new QuizService();

    // DB available flag
    private boolean dbAvailable = false;

    private AppContext() {}

    public static synchronized AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }

    public User getCurrentUser()           { return currentUser; }
    public void setCurrentUser(User user)  { this.currentUser = user; }
    public boolean isDbAvailable()         { return dbAvailable; }
    public void setDbAvailable(boolean v)  { this.dbAvailable = v; }
    public boolean isLoggedIn()            { return currentUser != null; }
    public void logout()                   { currentUser = null; }
}
