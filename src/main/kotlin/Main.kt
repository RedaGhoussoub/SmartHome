import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun main() {
    val smartHome = SmartHome(
        SmartTvDevice(name = "Android TV", category = "Entertainment"),
        SmartLightDevice(name = "Google light", category = "Utility")
    )

    smartHome.turnOnTv()
    smartHome.turnOnLight()
    println("Total number of devices currently turned on: ${smartHome.deviceTurnOnCount}")
    println()

    smartHome.printSmartTvInfo()
    println()

    smartHome.increaseTvVolume()
    smartHome.changeTvChannelToNext()
    smartHome.increaseLightBrightness()
    println()

    smartHome.turnOffAllDevices()
    println("Total number of devices currently turned on: ${smartHome.deviceTurnOnCount}.")
}

class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
) : ReadWriteProperty<Any?, Int> {

    var backField = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return backField
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            backField = value
        }
    }
}

class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice
) {

    var deviceTurnOnCount = 0
        private set

    fun turnOnTv() {
        if (smartTvDevice.deviceStatus == "off") {
            deviceTurnOnCount++
            smartTvDevice.turnOn()
        }
    }

    fun turnOffTv() {
        if (smartTvDevice.deviceStatus == "on") {
            deviceTurnOnCount--
            smartTvDevice.turnOff()
        }
    }

    fun increaseTvVolume() {
        if (smartTvDevice.deviceStatus == "on") {
            smartTvDevice.increaseSpeakerVolume()
        }
    }

    fun changeTvChannelToNext() {
        if (smartTvDevice.deviceStatus == "on") {
            smartTvDevice.nextChannel()
        }
    }

    fun printSmartTvInfo() {
        smartTvDevice.printDeviceInfo()
    }

    fun turnOnLight() {
        if (smartLightDevice.deviceStatus == "off") {
            deviceTurnOnCount++
            smartLightDevice.turnOn()
        }
    }

    fun turnOffLight() {
        if (smartLightDevice.deviceStatus == "on") {
            deviceTurnOnCount--
            smartLightDevice.turnOff()
        }
    }

    fun increaseLightBrightness() {
        if (smartLightDevice.deviceStatus == "on") {
            smartLightDevice.increaseBrightness()
        }
    }

    fun turnOffAllDevices() {
        turnOffTv()
        turnOffLight()
    }
}

open class SmartDevice(val name: String, val category: String) {

    var deviceStatus = "off"
        private set

    open val deviceType = "unknown"

    constructor(name: String, category: String, statusCode: Int) : this(name, category) {
        deviceStatus = when (statusCode) {
            0 -> "off"
            1 -> "on"
            else -> "unknown"
        }
    }

    open fun turnOn() {
        deviceStatus = "on"
    }

    open fun turnOff() {
        deviceStatus = "off"
    }

    fun printDeviceInfo() {
        println("\"Device name: $name, category: $category, type: $deviceType\"")
    }
}

class SmartTvDevice(name: String, category: String) : SmartDevice(name, category) {

    private var speakerVolume by RangeRegulator(initialValue = 4, minValue = 0, maxValue = 100)
    private var channelNumber by RangeRegulator(initialValue = 10, minValue = 0, maxValue = 200)

    override val deviceType = "Smart Tv"

    override fun turnOn() {
        super.turnOn()
        println(
            "$name is turned on. Speaker volume is set to $speakerVolume and channel number is " +
                    "set to $channelNumber."
        )
    }

    override fun turnOff() {
        super.turnOff()
        println("$name turned off")
    }

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased to $speakerVolume.")
    }

    fun decreaseSpeakerVolume() {
        speakerVolume--
        println("Speaker volume decreased to $speakerVolume.")
    }

    fun nextChannel() {
        channelNumber++
        println("Channel number increased to $speakerVolume.")
    }

    fun previousChannel() {
        channelNumber--
        println("Channel number decreased to $speakerVolume.")
    }
}

class SmartLightDevice(name: String, category: String) : SmartDevice(name, category) {

    private var brightnessLevel by RangeRegulator(initialValue = 5, minValue = 0, maxValue = 100)

    override val deviceType = "Smart Light"

    fun increaseBrightness() {
        brightnessLevel++
        println("Brightness number increased to $brightnessLevel.")
    }

    fun decreaseBrightness() {
        brightnessLevel--
        println("Brightness number decreased to $brightnessLevel.")
    }

    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }

    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("Smart light turned off.")
    }
}