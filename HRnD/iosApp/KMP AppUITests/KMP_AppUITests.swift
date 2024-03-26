//
//  KMP_AppUITests.swift
//  KMP AppUITests
//
//  Created by Michal Guspiel on 25.3.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import XCTest

final class KMP_AppUITests: XCTestCase {

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.

        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() throws {
        // UI tests must launch the application that they test.
        let app = XCUIApplication()
        app.launch()

        // Use XCTAssert and related functions to verify your tests produce the correct results.
    }

    func testColdLaunchPerformance() throws {
        if #available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 7.0, *) {
            // This measures how long it takes to launch your application.
            measure(metrics: [XCTApplicationLaunchMetric(waitUntilResponsive: true)]) {
                XCUIApplication().launch()
            }
        }
    }
    
    func testScrollAnimationPerformance() {
        if #available(iOS 15.0, *) {
            let app = XCUIApplication()
            app.launch()
            let measureOptions = XCTMeasureOptions()
            measureOptions.invocationOptions = [.manuallyStop]
            measure(metrics: [XCTOSSignpostMetric.scrollingAndDecelerationMetric], options: measureOptions) {
                let collection = app
                print("Collection views: ", app.collectionViews.description)
                collection.swipeDown(velocity: .fast)
                stopMeasuring()
                collection.swipeUp(velocity: .fast)
            }
        }
    }
    
    func launchApp() {
        XCUIApplication().launch()
    }
    
    func testWarmLaunchPerformance() throws {
        if #available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 7.0, *) {
            // This measures how long it takes to launch your application.
            let app = XCUIApplication()
            app.launch()
            XCUIDevice.shared.press(XCUIDevice.Button.home)
            measure(metrics: [XCTClockMetric()]) {
            app.activate()
            app.terminate()
            }
        }
    }
}
