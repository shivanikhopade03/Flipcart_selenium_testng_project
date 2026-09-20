package listeners;

import com.aventstack.extentreports.Status;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportManager;

/**
 * Hooks into TestNG's lifecycle so every test's pass/fail/skip status is
 * pushed into the Extent report automatically -- no manual logging needed
 * inside each @Test method.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().log(Status.FAIL, "Test failed: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        ExtentReportManager.flush();
    }
}
