/**
 * 
 */
package junittest;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author gerrit
 */
@Suite
@SelectClasses ({JUnitTestA.class/*, JUnitTestB.class*/})
@IncludeTags ({
	"JUnitTestA_test1",
// "JUnitTestA_test2",
//	"JUnitTestA_test3",
//	"JUnitTestB_test1",
//	"JUnitTestB_test2",
//	"JUnitTestB_test3",
})
class JUnitTestsuite{}
