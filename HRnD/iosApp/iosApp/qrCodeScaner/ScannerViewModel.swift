//
//  ScannerViewModel.swift
//  iosApp
//
//  Created by Michal Guspiel on 15.3.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

class ScannerViewModel: ObservableObject {
    
    /// Defines how often we are going to try looking for a new QR-code in the camera feed.
    let scanInterval: Double = 1.0
    
    @Published var torchIsOn: Bool = false
    @Published var lastQrCode: String = ""
    
    
    func onFoundQrCode(_ code: String) {
        self.lastQrCode = code
    }
}
