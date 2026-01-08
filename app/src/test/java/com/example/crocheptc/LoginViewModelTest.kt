package com.example.crocheptc

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult as FirebaseAuthResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        mockAuth = mock()
        viewModel = LoginViewModel(mockAuth)
    }

    @Test
    fun `signIn with blank email shows error`() = runTest {
        viewModel.email = ""
        viewModel.password = "password"
        viewModel.signIn()
        val result = viewModel.authResult.first()
        assertTrue(result is AuthResult.Error)
        assertEquals("Email e senha são obrigatórios", (result as AuthResult.Error).message)
    }

    @Test
    fun `signIn with blank password shows error`() = runTest {
        viewModel.email = "user@example.com"
        viewModel.password = ""
        viewModel.signIn()
        val result = viewModel.authResult.first()
        assertTrue(result is AuthResult.Error)
        assertEquals("Email e senha são obrigatórios", (result as AuthResult.Error).message)
    }

    @Test
    fun `signIn success updates authResult to Success`() = runTest {
        // Arrange mock to invoke onComplete with successful task
        val successTask = mock<Task<FirebaseAuthResult>> {
            on { isSuccessful } doAnswer { true }
            on { exception } doAnswer { null }
            on { addOnCompleteListener(any<OnCompleteListener<FirebaseAuthResult>>()) } doAnswer { invocation ->
                val listener = invocation.getArgument<OnCompleteListener<FirebaseAuthResult>>(0)
                listener.onComplete(this.mock)
                this.mock
            }
        }
        Mockito.`when`(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(successTask)

        viewModel.email = "user@example.com"
        viewModel.password = "password"
        viewModel.signIn()

        // First emission should be Loading
        val first = viewModel.authResult.first()
        assertTrue(first is AuthResult.Loading)
        // Collect next emission after task completes
        val second = viewModel.authResult.first { it !is AuthResult.Loading }
        assertTrue(second is AuthResult.Success)
    }

    @Test
    fun `signIn failure updates authResult to Error`() = runTest {
        val failureTask = mock<Task<FirebaseAuthResult>> {
            on { isSuccessful } doAnswer { false }
            on { exception } doAnswer { java.lang.Exception("Invalid credentials") }
            on { addOnCompleteListener(any<OnCompleteListener<FirebaseAuthResult>>()) } doAnswer { invocation ->
                val listener = invocation.getArgument<OnCompleteListener<FirebaseAuthResult>>(0)
                listener.onComplete(this.mock)
                this.mock
            }
        }
        Mockito.`when`(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(failureTask)

        viewModel.email = "user@example.com"
        viewModel.password = "wrong"
        viewModel.signIn()

        val first = viewModel.authResult.first()
        assertTrue(first is AuthResult.Loading)
        val second = viewModel.authResult.first { it !is AuthResult.Loading }
        assertTrue(second is AuthResult.Error)
        assertEquals("Invalid credentials", (second as AuthResult.Error).message)
    }
}
