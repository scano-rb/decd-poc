const http = require('http')

const host = '0.0.0.0'
const port = 8080

const MAX_ALLOWED_AMOUNT = 5000

/**
 * This process mock performs the payment processing using just the following rule:
 * 
 * if payment amount is less than 4000, payment is approved. Otherwise, it is rejected
 * 
 */
const processTx = (req, res) => {
    console.log('Processing payment...')

    let body = ''
    req.on('data', chunk => {
        body += chunk.toString()
    })
    req.on('end', () => {
        let tx = JSON.parse(body)

        let status = tx.amount < MAX_ALLOWED_AMOUNT ? "APPROVED" : "REJECTED"

        console.log(`transaction with amount: ${tx.amount} was: ${status}`)

        let responseBody = {"status": status}

        res.writeHead(200, {'Content-Type': 'application/json'})
        res.end(JSON.stringify(responseBody))
    })
}

const requestHandler = (req, res) => {
    if (req.method === 'POST') {
        processTx(req, res)
    }
}

const server = http.createServer(requestHandler)

server.listen(port, host, () => console.log(`Credit Card Mock Server running on http://${host}:${port}`))