import yfinance as yf
import pandas as pd
from datetime import datetime, timedelta
import pytz

def get_last_market_day():
    """Returns the most recent weekday market day (ignores holidays)."""
    today = datetime.now(pytz.timezone('US/Eastern')).date()
    offset = 0

    # Check backward for last weekday with data
    while offset < 10:
        test_date = today - timedelta(days=offset)
        test_data = yf.download("SPY", start=test_date.strftime("%Y-%m-%d"), end=(test_date + timedelta(days=1)).strftime("%Y-%m-%d"))
        if not test_data.empty:
            return test_date
        offset += 1

    raise Exception("No recent market day found in last 10 days.")

def download_stock_data(ticker: str, years: int = 6):
    last_market_date = get_last_market_day()
    start_date = last_market_date - timedelta(days=365 * years)

    print(f"Fetching {ticker} from {start_date} to {last_market_date}...")

    df = yf.download(ticker, start=start_date.strftime("%Y-%m-%d"), end=(last_market_date + timedelta(days=1)).strftime("%Y-%m-%d"))

    if df.empty:
        print("No data found.")
        return

    csv_filename = f"{ticker}_{start_date}_{last_market_date}.csv"
    df.to_csv(csv_filename)
    print(f"Saved to {csv_filename}")


def download_dividends(symbol, years=6):
    end_date = get_last_trading_day()
    start_date = end_date - timedelta(days=years*365)

    stock = yf.Ticker(symbol)
    dividends = stock.dividends

    if dividends.empty:
        print(f"No dividend data found for {symbol}")
        return

    # Filter date range
    dividends = dividends[(dividends.index.date >= start_date) & (dividends.index.date <= end_date)]

    # Save to CSV
    output_file = f"{symbol}_dividends.csv"
    dividends.to_frame(name="Dividend").to_csv(output_file, index_label="Date")
    print(f"Saved dividend data to {output_file}")

# Example usage
download_stock_data("AAPL", years=6)
download_stock_data("MSFT", years=6)
download_stock_data("TSLA", years=6)
download_stock_data("NVDA", years=6)
download_stock_data("AMZN", years=6)
download_stock_data("ENB", years=6)
download_stock_data("ORCL", years=6)
download_stock_data("QQQ", years=6)
download_stock_data("SPY", years=6)
download_stock_data("JPM", years=6)
download_stock_data("COIN", years=6)