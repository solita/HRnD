//
//  Test.swift
//  iosApp
//
//  Created by Michal Guspiel on 15.3.2024.
//  Copyright © 2024 orgName. All rights reserved.

import SwiftUI

class QRCodeScannerController: UIHostingController<ScannerView> {
    // Optionally, you can add custom initialization or configuration here
    
    init(passValue : @escaping (String) -> () ) {
        super.init(rootView: ScannerView(qrCodeAction: passValue))
    }
        
    // Required initializer when subclassing UIHostingController
    @objc required dynamic init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder, rootView: ScannerView(qrCodeAction: {_ in }))
    }
}
