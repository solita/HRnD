//
//  ScannerView.swift
//  iosApp
//
//  Created by Michal Guspiel on 15.3.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
struct ScannerView: View {

    var qrCodeAction: (String)-> Void
    @ObservedObject var scannerViewModel = ScannerViewModel()
    var body: some View {
        ZStack {
            Text("Scanner goes here...")
            QrCodeScannerView()
            .found(r: scannerViewModel.onFoundQrCode)
            .torchLight(isOn: scannerViewModel.torchIsOn)
            .interval(delay: scannerViewModel.scanInterval)
            .onReceive(scannerViewModel.$lastQrCode) { value in
                if value != "" {
                    qrCodeAction(value)
                    scannerViewModel.lastQrCode = ""
                }
            }
            VStack {
                VStack {
                    Text("Keep scanning for QR-codes")
                        .font(.subheadline)
                    Text(self.scannerViewModel.lastQrCode)
                        .bold()
                        .lineLimit(5)
                        .padding()
                }
                .padding(.vertical, 20)
                
                Spacer()
                HStack {
                    Button(action: {
                        scannerViewModel.torchIsOn.toggle()
                    }, label: {
                        Image(systemName: self.scannerViewModel.torchIsOn ? "bolt.fill" : "bolt.slash.fill")
                            .imageScale(.large)
                            .foregroundColor(scannerViewModel.torchIsOn ? Color.yellow : Color.blue)
                            .padding()
                    })
                }
                .background(Color.white)
                .cornerRadius(10)
                
            }.padding()
        }
    }
}

struct ScannerView_Previews: PreviewProvider {
    static var previews: some View {
        ScannerView(qrCodeAction: {_ in})
    }
}
