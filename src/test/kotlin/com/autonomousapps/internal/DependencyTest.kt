package com.autonomousapps.internal

import com.autonomousapps.advice.Dependency
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * The [Dependency] model object has custom `equals`, etc, and we want to preserve that behavior.
 */
class DependencyTest {

  private val orgDotSomethingGroup = "org.something"
  private val orgDotSomethingV1 = Dependency("$orgDotSomethingGroup:artifact", "1.0")
  private val orgDotSomethingV2 = Dependency("$orgDotSomethingGroup:artifact", "2.0")

  private val comDotSomethingV1 = Dependency("com.something:artifact", "1.0")

  private val projA = Dependency(":a")
  private val projB = Dependency(":b")

  @Suppress("ReplaceCallWithBinaryOperator")
  @Test fun compareTo() {
    assertTrue("comparison should only depend on identifier") {
      orgDotSomethingV1.compareTo(orgDotSomethingV2) == 0
    }

    assertTrue("com should come before org") {
      orgDotSomethingV1.compareTo(comDotSomethingV1) > 0
    }

    assertTrue("com should come before org") {
      comDotSomethingV1.compareTo(orgDotSomethingV1) < 0
    }
  }

  @Test fun testToString() {
    assertTrue { projA.toString() == ":a" }
    assertTrue { projB.toString() == ":b" }
    assertTrue { orgDotSomethingV1.toString() == "org.something:artifact:1.0" }
  }

  @Test fun testEqualsAndHashCode() {
    assertTrue("equality should only depend on identifier") {
      orgDotSomethingV1 == orgDotSomethingV2
    }
    assertTrue("hash code should only depend on identifier") {
      orgDotSomethingV1.hashCode() == orgDotSomethingV2.hashCode()
    }

    assertFalse("equality does not depend on the version") {
      orgDotSomethingV1 == comDotSomethingV1
    }
    assertFalse("hash code does not depend on version") {
      orgDotSomethingV1.hashCode() == comDotSomethingV1.hashCode()
    }

    assertFalse("project equality") {
      projA == projB
    }
  }

  @Test fun facade() {
    assertThat(orgDotSomethingV1.group).isEqualTo(orgDotSomethingGroup)
    assertThat(projA.group).isEqualTo(null)
  }
}