package org.catrobat.paintroid.test.espresso

import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.catrobat.paintroid.MainActivity
import org.catrobat.paintroid.R
import org.catrobat.paintroid.common.Constants
import org.catrobat.paintroid.test.espresso.util.DrawingSurfaceLocationProvider
import org.catrobat.paintroid.test.espresso.util.EspressoUtils
import org.catrobat.paintroid.test.espresso.util.UiInteractions
import org.catrobat.paintroid.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView
import org.catrobat.paintroid.test.espresso.util.wrappers.TopBarViewInteraction
import org.catrobat.paintroid.test.utils.ScreenshotOnFailRule
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf
import org.junit.*
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class CatrobatImageIOIntegrationTest {

    @get:Rule
    val launchActivityRule = ActivityTestRule(MainActivity::class.java)

    @get:Rule
    val screenshotOnFailRule = ScreenshotOnFailRule()

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = EspressoUtils.grantPermissionRulesVersionCheck()

    private lateinit var uriFile: Uri
    private lateinit var activity: MainActivity

    companion object {
        private const val IMAGE_NAME = "fileName"
    }
    @Before
    fun setUp() {
        activity = launchActivityRule.activity
    }

    @After
    fun tearDown() {
        with(File(uriFile.path!!)) {
            if (exists()) {
                delete()
            }
        }
    }

    @Test
    fun testWriteAndReadCatrobatImage() {
        onDrawingSurfaceView()
                .perform(UiInteractions.touchAt(DrawingSurfaceLocationProvider.MIDDLE))
        TopBarViewInteraction.onTopBarView()
                .performOpenMoreOptions()
        onView(withText(R.string.menu_save_image))
                .perform(ViewActions.click())
        onView(withId(R.id.pocketpaint_save_dialog_spinner))
                .perform(ViewActions.click())
        Espresso.onData(AllOf.allOf(Matchers.`is`(Matchers.instanceOf<Any>(String::class.java)),
                Matchers.`is`<String>(Constants.CATROBAT_IMAGE_ENDING))).inRoot(RootMatchers.isPlatformPopup()).perform(ViewActions.click())
        onView(withId(R.id.pocketpaint_image_name_save_text))
                .perform(replaceText(IMAGE_NAME))
        onView(withText(R.string.save_button_text))
                .perform(ViewActions.click())
        uriFile = activity.model.savedPictureUri
        Assert.assertNotNull(uriFile)
        Assert.assertNotNull(activity.workspace.commandSerializationHelper.readFromFile(uriFile))
    }
}