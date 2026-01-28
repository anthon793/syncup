package com.syncup.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syncup.domain.model.TaskStatus
import com.syncup.presentation.viewmodel.ProjectViewModel

@Composable
fun ProjectWorkspaceScreen(
    projectId: String,
    onBackClick: () -> Unit = {},
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val currentProject = viewModel.currentProject.collectAsState()
    val todoTasks = viewModel.todoTasks.collectAsState()
    val inProgressTasks = viewModel.inProgressTasks.collectAsState()
    val criticalTasks = viewModel.criticalTasks.collectAsState()
    val doneTasks = viewModel.doneTasks.collectAsState()

    // Load project when screen is shown
    androidx.compose.runtime.LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Project Header
            currentProject.value?.let { project ->
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Kanban Board
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                KanbanColumn(
                    title = "To Do",
                    tasks = todoTasks.value,
                    backgroundColor = Color(0xFFE3F2FD),
                    onTaskClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                )

                KanbanColumn(
                    title = "In Progress",
                    tasks = inProgressTasks.value,
                    backgroundColor = Color(0xFFFFF3E0),
                    onTaskClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                )

                KanbanColumn(
                    title = "Critical",
                    tasks = criticalTasks.value,
                    backgroundColor = Color(0xFFFFEBEE),
                    onTaskClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                )

                KanbanColumn(
                    title = "Done",
                    tasks = doneTasks.value,
                    backgroundColor = Color(0xFFE8F5E9),
                    onTaskClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }

        // FAB for adding new task
        FloatingActionButton(
            onClick = { /* Show create task dialog */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Task")
        }
    }
}

@Composable
fun KanbanColumn(
    title: String,
    tasks: List<com.syncup.domain.model.Task>,
    backgroundColor: Color,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tasks) { task ->
                KanbanTaskCard(
                    task = task,
                    onTaskClick = onTaskClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun KanbanTaskCard(
    task: com.syncup.domain.model.Task,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        onClick = { onTaskClick(task.id) }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = task.priority.name,
                style = MaterialTheme.typography.labelSmall,
                color = when (task.priority) {
                    com.syncup.domain.model.TaskPriority.CRITICAL -> MaterialTheme.colorScheme.error
                    com.syncup.domain.model.TaskPriority.HIGH -> Color(0xFFFF9800)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(top = 4.dp)
            )

            if (task.blockerReason != null) {
                Text(
                    text = "🚫 Blocker",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
