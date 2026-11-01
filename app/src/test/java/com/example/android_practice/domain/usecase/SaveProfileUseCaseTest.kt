package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.error.InvalidUrlException
import com.example.android_practice.domain.model.UserProfile
import com.example.android_practice.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveProfileUseCaseTest {

    private class FakeProfileRepository : ProfileRepository {
        var savedProfile: UserProfile? = null
        override val profile: Flow<UserProfile> = flowOf(UserProfile())
        override suspend fun save(profile: UserProfile) {
            savedProfile = profile
        }
    }

    @Test
    fun testUrlNormalization_addsHttpsAndTrims() = runBlocking {
        val repo = FakeProfileRepository()
        val useCase = SaveProfileUseCase(repo)

        useCase(UserProfile(fullName = "  John Doe  ", resumeUrl = " example.com/resume.pdf "))

        assertEquals("John Doe", repo.savedProfile?.fullName)
        assertEquals("https://example.com/resume.pdf", repo.savedProfile?.resumeUrl)
    }

    @Test
    fun testUrlNormalization_upgradesHttpToHttps() = runBlocking {
        val repo = FakeProfileRepository()
        val useCase = SaveProfileUseCase(repo)

        useCase(UserProfile(fullName = "Jane", resumeUrl = "http://mywebsite.org/cv.pdf"))

        assertEquals("https://mywebsite.org/cv.pdf", repo.savedProfile?.resumeUrl)
    }

    @Test(expected = InvalidUrlException::class)
    fun testUrlNormalization_throwsOnInvalidHost() = runBlocking {
        val repo = FakeProfileRepository()
        val useCase = SaveProfileUseCase(repo)

        useCase(UserProfile(fullName = "Test", resumeUrl = "not_a_valid_domain"))
    }
}
