package br.com.bluetook.helper

import android.bluetooth.BluetoothAdapter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BluetooKHelperTest {

    @RelaxedMockK private lateinit var bluetoothAdapter: BluetoothAdapter

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given bluetooth adapter enabled, then bluetooth is available`() {
        every { bluetoothAdapter.isEnabled } returns true

        val bluetoothAvailable = BluetooKHelper(bluetoothAdapter).bluetoothAvailable()
        assert(bluetoothAvailable)
    }

    @Test
    fun `given bluetooth adapter disabled, then bluetooth is unavailable`() {
        every { bluetoothAdapter.isEnabled } returns false

        val bluetoothAvailable = BluetooKHelper(bluetoothAdapter).bluetoothAvailable()
        assertFalse(bluetoothAvailable)
    }

    @AfterEach
    internal fun tearDown() {
        unmockkAll()
    }
}