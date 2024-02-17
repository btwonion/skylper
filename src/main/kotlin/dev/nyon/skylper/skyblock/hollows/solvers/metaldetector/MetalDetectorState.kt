package dev.nyon.skylper.skyblock.hollows.solvers.metaldetector

interface MetalDetectorState {
    object Idle : MetalDetectorState
    object Solving : MetalDetectorState
}