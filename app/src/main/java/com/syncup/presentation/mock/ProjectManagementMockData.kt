package com.syncup.presentation.mock

import com.syncup.domain.model.*
import com.syncup.domain.service.ChatService
import com.syncup.domain.service.MilestoneService
import com.syncup.domain.service.TaskService

/**
 * Comprehensive mock data demonstrating role-based project management
 * with hardcoded users, projects, tasks, milestones, and chat messages
 */
object ProjectManagementMockData {
    
    // ============== HARDCODED USERS (PERSONAS) ==============
    
    /** Tunde – Project Lead (ADMIN) */
    val tundeLeadUser = RoleBasedUser(
        id = "user_tunde",
        name = "Tunde",
        email = "tunde@university.edu",
        role = UserRole.ADMIN
    )
    
    /** Sarah – Team Member (MEMBER) */
    val sarahTeamMember = RoleBasedUser(
        id = "user_sarah",
        name = "Sarah",
        email = "sarah@university.edu",
        role = UserRole.MEMBER,
        joinDate = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000) // 7 days ago
    )
    
    /** Ahmed – Late Joiner (MEMBER) */
    val ahmedLateJoiner = RoleBasedUser(
        id = "user_ahmed",
        name = "Ahmed",
        email = "ahmed@university.edu",
        role = UserRole.MEMBER,
        joinDate = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000) // 2 days ago
    )
    
    val allUsers = listOf(tundeLeadUser, sarahTeamMember, ahmedLateJoiner)
    
    // ============== HARDCODED PROJECT ==============
    
    val mainProject = Project(
        id = "proj_001",
        name = "Student Collaboration Platform",
        description = "A comprehensive project management and collaboration tool for university students",
        owner = "user_tunde",
        members = listOf("user_tunde", "user_sarah", "user_ahmed"),
        coverColor = "#1DB584",
        dueDate = System.currentTimeMillis() + (60 * 24 * 60 * 60 * 1000), // 60 days from now
        createdAt = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000) // Started 30 days ago
    )
    
    // ============== HARDCODED MILESTONES ==============
    
    val milestones = listOf(
        Milestone(
            id = "mile_001",
            projectId = "proj_001",
            title = "Phase 1: Planning & Design",
            description = "Initial project planning, requirement gathering, and UI/UX design",
            dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // 7 days
            order = 1
        ),
        Milestone(
            id = "mile_002",
            projectId = "proj_001",
            title = "Phase 2: Backend Development",
            description = "API development and database setup",
            dueDate = System.currentTimeMillis() + (21 * 24 * 60 * 60 * 1000), // 21 days
            order = 2
        ),
        Milestone(
            id = "mile_003",
            projectId = "proj_001",
            title = "Phase 3: Frontend Development",
            description = "Mobile app UI implementation",
            dueDate = System.currentTimeMillis() + (35 * 24 * 60 * 60 * 1000), // 35 days
            order = 3
        ),
        Milestone(
            id = "mile_004",
            projectId = "proj_001",
            title = "Phase 4: Testing & Deployment",
            description = "QA testing and production deployment",
            dueDate = System.currentTimeMillis() + (50 * 24 * 60 * 60 * 1000), // 50 days
            order = 4
        )
    )
    
    // ============== HARDCODED TASKS ==============
    
    val tasks = mutableListOf(
        // Phase 1 Tasks
        Task(
            id = "task_001",
            projectId = "proj_001",
            milestoneId = "mile_001",
            title = "Create project requirements document",
            description = "Document all functional and non-functional requirements",
            assignedTo = "user_tunde",
            dueDate = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.DONE,
            createdBy = "user_tunde"
        ),
        Task(
            id = "task_002",
            projectId = "proj_001",
            milestoneId = "mile_001",
            title = "Design UI mockups",
            description = "Create Figma designs for all main screens",
            assignedTo = "user_sarah",
            dueDate = System.currentTimeMillis() + (4 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.IN_PROGRESS,
            createdBy = "user_tunde"
        ),
        Task(
            id = "task_003",
            projectId = "proj_001",
            milestoneId = "mile_001",
            title = "Create user flow diagrams",
            description = "Document the complete user journey through the app",
            assignedTo = "user_sarah",
            dueDate = System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.MEDIUM,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        ),
        
        // Phase 2 Tasks
        Task(
            id = "task_004",
            projectId = "proj_001",
            milestoneId = "mile_002",
            title = "Setup database schema",
            description = "Design and create MongoDB collections for users, projects, tasks",
            assignedTo = "user_ahmed",
            dueDate = System.currentTimeMillis() + (10 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        ),
        Task(
            id = "task_005",
            projectId = "proj_001",
            milestoneId = "mile_002",
            title = "Implement user authentication API",
            description = "REST endpoints for login, signup, and token refresh",
            assignedTo = "user_ahmed",
            dueDate = System.currentTimeMillis() + (12 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        ),
        Task(
            id = "task_006",
            projectId = "proj_001",
            milestoneId = "mile_002",
            title = "Implement task management endpoints",
            description = "Create, read, update, delete operations for tasks",
            assignedTo = "user_ahmed",
            dueDate = System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.HIGH,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        ),
        
        // Phase 3 Tasks
        Task(
            id = "task_007",
            projectId = "proj_001",
            milestoneId = "mile_003",
            title = "Implement authentication screens",
            description = "Login, signup, and password reset screens in Kotlin",
            assignedTo = "user_sarah",
            dueDate = System.currentTimeMillis() + (20 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        ),
        Task(
            id = "task_008",
            projectId = "proj_001",
            milestoneId = "mile_003",
            title = "Implement task dashboard",
            description = "Build the main dashboard showing all tasks and filters",
            assignedTo = "user_sarah",
            dueDate = System.currentTimeMillis() + (25 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        ),
        Task(
            id = "task_009",
            projectId = "proj_001",
            milestoneId = "mile_003",
            title = "Implement chat interface",
            description = "Real-time chat UI for project and task discussions",
            assignedTo = "user_sarah",
            dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.MEDIUM,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        ),
        
        // Phase 4 Tasks
        Task(
            id = "task_010",
            projectId = "proj_001",
            milestoneId = "mile_004",
            title = "Write test suite",
            description = "Unit tests and integration tests for all features",
            assignedTo = "user_ahmed",
            dueDate = System.currentTimeMillis() + (40 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.HIGH,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        ),
        Task(
            id = "task_011",
            projectId = "proj_001",
            milestoneId = "mile_004",
            title = "Deploy to production",
            description = "Setup CI/CD pipeline and deploy to production",
            assignedTo = "user_tunde",
            dueDate = System.currentTimeMillis() + (50 * 24 * 60 * 60 * 1000),
            priority = TaskPriority.CRITICAL,
            status = TaskStatus.TODO,
            createdBy = "user_tunde"
        )
    )
    
    // ============== HARDCODED CHAT MESSAGES ==============
    
    fun initializeChatMessages(chatService: ChatService) {
        // Project-level discussion
        chatService.sendMessage(
            ConversationType.PROJECT_DISCUSSION,
            "proj_001",
            tundeLeadUser,
            "Welcome to the project team! Let's build something amazing together. 🚀"
        )
        
        chatService.sendMessage(
            ConversationType.PROJECT_DISCUSSION,
            "proj_001",
            sarahTeamMember,
            "Thanks Tunde! I've started sketching the UI designs. Will share by tomorrow."
        )
        
        chatService.sendMessage(
            ConversationType.PROJECT_DISCUSSION,
            "proj_001",
            tundeLeadUser,
            "Great! Looking forward to seeing your designs. Ahmed, can you start on the database setup?"
        )
        
        // Task-specific discussion for task_002
        chatService.sendMessage(
            ConversationType.TASK_DISCUSSION,
            "task_002",
            sarahTeamMember,
            "Started designing the dashboard. Need some feedback - should we use card-based or list-based layout?"
        )
        
        chatService.sendMessage(
            ConversationType.TASK_DISCUSSION,
            "task_002",
            tundeLeadUser,
            "I'd recommend card-based for better visual hierarchy. Check the requirements doc section 3.2"
        )
        
        // After Ahmed joins (simulating late joiner access to history)
        chatService.sendMessage(
            ConversationType.PROJECT_DISCUSSION,
            "proj_001",
            ahmedLateJoiner,
            "Hey team! Just joined. Reading through the docs now. I'll start the database setup this week."
        )
        
        chatService.sendMessage(
            ConversationType.PROJECT_DISCUSSION,
            "proj_001",
            tundeLeadUser,
            "Welcome Ahmed! Great to have you on board. Check the wiki for setup instructions."
        )
    }
    
    // ============== SERVICE INSTANCES ==============
    
    val taskService = TaskService()
    val milestoneService = MilestoneService()
    val chatService = ChatService().apply {
        initializeChatMessages(this)
    }
}
