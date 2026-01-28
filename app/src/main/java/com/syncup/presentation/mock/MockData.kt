package com.syncup.presentation.mock

import com.syncup.domain.model.*

object MockData {
    // Current logged-in user
    val mockUser = User(
        id = "user_1",
        email = "alex.student@university.edu",
        name = "Alex Student",
        university = "Tech University",
        profilePictureUrl = "https://example.com/avatar.jpg",
        isOnline = true,
        lastSeen = System.currentTimeMillis()
    )

    // Team members for the app
    val mockTeamMembers = listOf(
        TeamMember(
            id = "user_1",
            name = "Alex Student",
            email = "alex.student@university.edu",
            role = "The Lead",
            avatarUrl = null,
            isOnline = true,
            skills = listOf("UI/UX Design", "Python", "React Native", "Figma", "Agile", "Git", "Systems Arch"),
            tasksCompleted = 85,
            codeCommits = 65,
            designReviews = 92
        ),
        TeamMember(
            id = "user_2",
            name = "Sarah",
            email = "sarah@university.edu",
            role = "Designer",
            avatarUrl = null,
            isOnline = true,
            skills = listOf("UI Design", "Figma"),
            tasksCompleted = 78,
            codeCommits = 45,
            designReviews = 88
        ),
        TeamMember(
            id = "user_3",
            name = "Jamie",
            email = "jamie@university.edu",
            role = "Developer",
            avatarUrl = null,
            isOnline = true,
            skills = listOf("Kotlin", "Android"),
            tasksCompleted = 90,
            codeCommits = 85,
            designReviews = 60
        ),
        TeamMember(
            id = "user_4",
            name = "Mike",
            email = "mike@university.edu",
            role = "Developer",
            avatarUrl = null,
            isOnline = true,
            skills = listOf("Backend", "APIs"),
            tasksCompleted = 72,
            codeCommits = 90,
            designReviews = 55
        ),
        TeamMember(
            id = "user_5",
            name = "Lina",
            email = "lina@university.edu",
            role = "QA",
            avatarUrl = null,
            isOnline = false,
            skills = listOf("Testing", "Documentation"),
            tasksCompleted = 88,
            codeCommits = 30,
            designReviews = 70
        )
    )

    // Suggested students for invite
    val suggestedStudents = listOf(
        SuggestedStudent(
            id = "s1",
            name = "Jordan Smith",
            email = "jsmith@university.edu",
            avatarUrl = null,
            isSelected = false
        ),
        SuggestedStudent(
            id = "s2",
            name = "Emily Chen",
            email = "echen@university.edu",
            avatarUrl = null,
            isSelected = true
        ),
        SuggestedStudent(
            id = "s3",
            name = "Marcus Thompson",
            email = "m.thom@university.edu",
            avatarUrl = null,
            isSelected = false
        ),
        SuggestedStudent(
            id = "s4",
            name = "Sarah Miller",
            email = "smiller@university.edu",
            avatarUrl = null,
            isSelected = false
        )
    )

    // Projects with enhanced data
    val mockProjects = listOf(
        Project(
            id = "project_1",
            name = "Marketing Capstone",
            description = "Final Submission",
            owner = "user_1",
            members = listOf("user_1", "user_2", "user_3", "user_4", "user_5"),
            coverColor = "#2D5F5F",
            createdAt = System.currentTimeMillis() - 86400000 * 30,
            updatedAt = System.currentTimeMillis(),
            isArchived = false
        ),
        Project(
            id = "project_2",
            name = "Biology Lab Report",
            description = "BIO 101 - Group A",
            owner = "user_1",
            members = listOf("user_1", "user_2"),
            coverColor = "#1A5F5F",
            createdAt = System.currentTimeMillis() - 172800000,
            updatedAt = System.currentTimeMillis() - 3600000,
            isArchived = false
        ),
        Project(
            id = "project_3",
            name = "UX Research",
            description = "DES 200",
            owner = "user_1",
            members = listOf("user_1", "user_3", "user_4"),
            coverColor = "#F5E6D3",
            createdAt = System.currentTimeMillis() - 86400000 * 7,
            updatedAt = System.currentTimeMillis() - 7200000,
            isArchived = false
        )
    )

    // Focus projects for dashboard
    val focusProjects = listOf(
        FocusProject(
            id = "project_2",
            name = "Biology Lab Report",
            courseCode = "BIO 101",
            progress = 65,
            imageRes = "microscope",
            isActive = true
        ),
        FocusProject(
            id = "project_3",
            name = "UX Research",
            courseCode = "DES 200",
            progress = 40,
            imageRes = "design",
            isActive = false
        )
    )

    // Priority project info for dashboard card
    val priorityProject = PriorityProjectInfo(
        projectId = "project_1",
        projectName = "Marketing Capstone",
        nextMilestone = "Final Submission",
        hoursRemaining = 48,
        totalMembers = 5,
        isTimeCrunch = true
    )

    // Tasks organized by status
    val mockTasks = listOf(
        // TO-DO Tasks
        Task(
            id = "task_1",
            projectId = "project_1",
            milestoneId = null,
            title = "Finalize Physics Research",
            description = "Cross-reference lab data with the theoretical model for final submission.",
            assignedTo = "user_1",
            dueDate = System.currentTimeMillis() + 3600000, // Due today
            status = TaskStatus.TODO,
            priority = TaskPriority.CRITICAL,
            labels = listOf("research", "urgent"),
            attachments = listOf(),
            blockerReason = null,
            createdBy = "user_1",
            createdAt = System.currentTimeMillis() - 86400000,
            updatedAt = System.currentTimeMillis()
        ),
        // DOING Tasks
        Task(
            id = "task_2",
            projectId = "project_1",
            milestoneId = null,
            title = "Weekly Sync Preparation",
            description = "Prepare agenda and materials for weekly sync meeting.",
            assignedTo = "user_2",
            dueDate = System.currentTimeMillis() + 172800000,
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.MEDIUM,
            labels = listOf("development"),
            attachments = listOf(),
            blockerReason = null,
            createdBy = "user_1",
            createdAt = System.currentTimeMillis() - 7200000,
            updatedAt = System.currentTimeMillis()
        ),
        Task(
            id = "task_3",
            projectId = "project_1",
            milestoneId = null,
            title = "UI Component Audit",
            description = "Reviewing consistency across all glassmorphism elements.",
            assignedTo = "user_3",
            dueDate = System.currentTimeMillis() + 86400000,
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.MEDIUM,
            labels = listOf("design"),
            attachments = listOf(),
            blockerReason = null,
            createdBy = "user_2",
            createdAt = System.currentTimeMillis() - 14400000,
            updatedAt = System.currentTimeMillis() - 3600000
        ),
        // DONE Tasks
        Task(
            id = "task_4",
            projectId = "project_1",
            milestoneId = "milestone_1",
            title = "Research Phase Complete",
            description = "Research submitted and verified by team lead.",
            assignedTo = "user_2",
            dueDate = System.currentTimeMillis() - 86400000,
            status = TaskStatus.DONE,
            priority = TaskPriority.HIGH,
            labels = listOf("research", "completed"),
            attachments = listOf("research_doc.pdf"),
            blockerReason = null,
            createdBy = "user_2",
            createdAt = System.currentTimeMillis() - 604800000,
            updatedAt = System.currentTimeMillis() - 86400000
        ),
        Task(
            id = "task_5",
            projectId = "project_1",
            milestoneId = null,
            title = "Draft Project Abstract",
            description = "Summary draft completed for peer review.",
            assignedTo = "user_4",
            dueDate = System.currentTimeMillis() - 172800000,
            status = TaskStatus.DONE,
            priority = TaskPriority.MEDIUM,
            labels = listOf("documentation"),
            attachments = listOf(),
            blockerReason = null,
            createdBy = "user_1",
            createdAt = System.currentTimeMillis() - 432000000,
            updatedAt = System.currentTimeMillis() - 172800000
        ),
        Task(
            id = "task_6",
            projectId = "project_1",
            milestoneId = null,
            title = "Resource Allocation Plan",
            description = "Budget and materials distributed among team members.",
            assignedTo = "user_1",
            dueDate = System.currentTimeMillis() - 259200000,
            status = TaskStatus.DONE,
            priority = TaskPriority.LOW,
            labels = listOf("planning"),
            attachments = listOf(),
            blockerReason = null,
            createdBy = "user_1",
            createdAt = System.currentTimeMillis() - 518400000,
            updatedAt = System.currentTimeMillis() - 259200000
        ),
        // BACKLOG Tasks
        Task(
            id = "task_7",
            projectId = "project_1",
            milestoneId = null,
            title = "Gamification Module Study",
            description = "Research how to implement XP points for completing study sessions.",
            assignedTo = "user_1",
            dueDate = null,
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            labels = listOf("idea", "future"),
            attachments = listOf(),
            blockerReason = null,
            createdBy = "user_1",
            createdAt = System.currentTimeMillis() - 86400000,
            updatedAt = System.currentTimeMillis() - 86400000
        ),
        Task(
            id = "task_8",
            projectId = "project_1",
            milestoneId = null,
            title = "Team Portfolio Layout",
            description = "Design a centralized place to showcase all project deliverables.",
            assignedTo = null,
            dueDate = null,
            status = TaskStatus.TODO,
            priority = TaskPriority.LOW,
            labels = listOf("design", "future"),
            attachments = listOf(),
            blockerReason = null,
            createdBy = "user_2",
            createdAt = System.currentTimeMillis() - 172800000,
            updatedAt = System.currentTimeMillis() - 172800000
        ),
        Task(
            id = "task_9",
            projectId = "project_1",
            milestoneId = null,
            title = "API Documentation Update",
            description = "Future task: Sync documentation with the upcoming version 2.0 release.",
            assignedTo = "user_3",
            dueDate = null,
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            labels = listOf("documentation", "api"),
            attachments = listOf(),
            blockerReason = null,
            createdBy = "user_3",
            createdAt = System.currentTimeMillis() - 259200000,
            updatedAt = System.currentTimeMillis() - 259200000
        )
    )

    // Sub-groups for Team screen
    val subGroups = listOf(
        SubGroup(
            id = "sg1",
            name = "UI/UX Design",
            icon = "🎨",
            status = SubGroupStatus.IN_PROGRESS,
            capacity = 4,
            currentMembers = 2,
            workload = WorkloadLevel.MODERATE,
            completionPercentage = 85
        ),
        SubGroup(
            id = "sg2",
            name = "Development",
            icon = "💻",
            status = SubGroupStatus.DEADLINE_SOON,
            capacity = 4,
            currentMembers = 4,
            workload = WorkloadLevel.HIGH,
            completionPercentage = 42
        ),
        SubGroup(
            id = "sg3",
            name = "Backend API",
            icon = "⚙️",
            status = SubGroupStatus.STABLE,
            capacity = 3,
            currentMembers = 1,
            workload = WorkloadLevel.LOW,
            completionPercentage = 60
        ),
        SubGroup(
            id = "sg4",
            name = "Docs & Wiki",
            icon = "📄",
            status = SubGroupStatus.DONE,
            capacity = 2,
            currentMembers = 2,
            workload = WorkloadLevel.LOW,
            completionPercentage = 100
        )
    )

    // Milestones
    val mockMilestones = listOf(
        Milestone(
            id = "milestone_1",
            projectId = "project_1",
            title = "Research Phase",
            description = "Complete all initial research and gather sources",
            dueDate = System.currentTimeMillis() - 86400000,
            progress = 1.0f,
            order = 1,
            isCompleted = true
        ),
        Milestone(
            id = "milestone_2",
            projectId = "project_1",
            title = "Final Submission",
            description = "Submit final deliverables",
            dueDate = System.currentTimeMillis() + 172800000,
            progress = 0.65f,
            order = 2,
            isCompleted = false
        )
    )

    // Activities for Team Pulse / Collaborate Feed
    val mockActivities = listOf(
        Activity(
            id = "activity_1",
            projectId = "project_1",
            userId = "user_2",
            userName = "Sarah Jenkins",
            timestamp = System.currentTimeMillis() - 1200000, // 20 min ago
            type = ActivityType.FILE_UPLOAD,
            title = "File uploaded",
            description = "uploaded UI Style Guide v2.pdf",
            data = mapOf("fileName" to "UI Style Guide v2.pdf", "fileSize" to 2.5)
        ),
        Activity(
            id = "activity_2",
            projectId = "project_1",
            userId = "user_2",
            userName = "Sarah Jenkins",
            timestamp = System.currentTimeMillis() - 2400000, // 40 min ago
            type = ActivityType.MILESTONE,
            title = "Research Phase Complete",
            description = "Compiled all sources and citations. Ready for drafting!",
            data = mapOf("milestone" to "Research Phase", "status" to "completed")
        ),
        Activity(
            id = "activity_3",
            projectId = "project_1",
            userId = "user_4",
            userName = "Mike Chen",
            timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
            type = ActivityType.COMMENT,
            title = "Comment",
            description = "Great work Sarah! 👏 Reviewing the outline tonight.",
            data = mapOf()
        ),
        Activity(
            id = "activity_4",
            projectId = "project_1",
            userId = "system",
            userName = "SyncUp",
            timestamp = System.currentTimeMillis() - 7200000, // 2 hours ago
            type = ActivityType.ALERT,
            title = "Team Inactivity Alert",
            description = "The \"Front-end\" subgroup hasn't updated in 48 hours. Keep the momentum going!",
            data = mapOf("subgroup" to "Front-end", "hours" to 48)
        ),
        Activity(
            id = "activity_5",
            projectId = "project_1",
            userId = "user_1",
            userName = "Alex Rivera",
            timestamp = System.currentTimeMillis() - 9000000, // 2.5 hours ago
            type = ActivityType.BLOCKER,
            title = "Formatting Stuck",
            description = "Table alignment keeps breaking on export. Need a hand with the CSS!",
            data = mapOf("severity" to "high")
        )
    )

    // Quick stats for dashboard
    val quickStats = QuickStats(
        tasksCompletedToday = 8,
        activeProjects = 3,
        onlineTeammates = 4,
        upcomingDeadlines = 2
    )
}

// Data classes for mock data
data class TeamMember(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val avatarUrl: String?,
    val isOnline: Boolean,
    val skills: List<String> = emptyList(),
    val tasksCompleted: Int = 0,
    val codeCommits: Int = 0,
    val designReviews: Int = 0
) {
    // Computed properties for avatar display
    val avatar: String get() = name.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")
    val avatarColorValue: Long get() = when (kotlin.math.abs(name.hashCode()) % 12) {
        0 -> 0xFF1DB584
        1 -> 0xFF6366F1
        2 -> 0xFFEC4899
        3 -> 0xFFF59E0B
        4 -> 0xFF8B5CF6
        5 -> 0xFF14B8A6
        6 -> 0xFFEF4444
        7 -> 0xFF3B82F6
        8 -> 0xFF10B981
        9 -> 0xFFF97316
        10 -> 0xFF06B6D4
        else -> 0xFFD946EF
    }
}

data class SuggestedStudent(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val isSelected: Boolean
)

data class FocusProject(
    val id: String,
    val name: String,
    val courseCode: String,
    val progress: Int,
    val imageRes: String,
    val isActive: Boolean
)

data class PriorityProjectInfo(
    val projectId: String,
    val projectName: String,
    val nextMilestone: String,
    val hoursRemaining: Int,
    val totalMembers: Int,
    val isTimeCrunch: Boolean
)

data class SubGroup(
    val id: String,
    val name: String,
    val icon: String,
    val status: SubGroupStatus,
    val capacity: Int,
    val currentMembers: Int,
    val workload: WorkloadLevel,
    val completionPercentage: Int
)

enum class SubGroupStatus {
    IN_PROGRESS, DEADLINE_SOON, STABLE, DONE
}

enum class WorkloadLevel {
    LOW, MODERATE, HIGH
}

data class Activity(
    val id: String,
    val projectId: String,
    val userId: String,
    val userName: String,
    val timestamp: Long,
    val type: ActivityType,
    val title: String,
    val description: String,
    val data: Map<String, Any>
)

enum class ActivityType {
    FILE_UPLOAD, MILESTONE, COMMENT, ALERT, BLOCKER, TASK_UPDATE
}

data class QuickStats(
    val tasksCompletedToday: Int,
    val activeProjects: Int,
    val onlineTeammates: Int,
    val upcomingDeadlines: Int
)
