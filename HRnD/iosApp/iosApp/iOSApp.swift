import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        ScannerFactoryCompanion().shared = TheFactory()
        KoinKt.doInitKoin()
    }

    var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
