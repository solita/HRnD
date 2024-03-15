//
//  TheFactory.swift
//  iosApp
//
//  Created by Michal Guspiel on 15.3.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit
import ComposeApp

class TheFactory: ScannerFactory {
    func makeController(passValue : @escaping (String) -> () ) -> UIViewController {
        QRCodeScannerController(passValue: passValue)
    }
}
